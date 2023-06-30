package br.aranda.controller;

import br.aranda.domain.entity.Usuario;
import br.aranda.dto.in.CredenciaisDTO;
import br.aranda.dto.out.TokenDTO;
import br.aranda.exception.SenhaInvalidaException;
import br.aranda.security.jwt.JwtService;
import br.aranda.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Api("Usuarios Api")
public class UsuarioController {
    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/salvar")
    @ResponseStatus(CREATED)
    @ApiOperation("Atualiza um usuário")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Usuário atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Usuário não encontrado para o id informado")
    })
    public Usuario salvar(@RequestBody @Valid Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return usuarioService.salvar(usuario);
    }

    @GetMapping("listar-usuarios")
    @ApiOperation("Obter listagem dos usuários cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário(s) encontrado(s)"),
            @ApiResponse(code = 400, message = "Nenhum usuário encontrado na base de dados")
    })
    public List<Usuario> getUsuarios() {
        return usuarioService.getUsuarios();
    }

    @PostMapping("/auth")
    @ResponseStatus(CREATED)
    @ApiOperation("Autenticar o usuário no sistema")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Usuário autenticado com sucesso"),
            @ApiResponse(code = 400, message = "Usuário não encontrado para o id informado")
    })
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciaisDTO) {
        try {
            Usuario usuario = Usuario.builder()
                    .login(credenciaisDTO.getLogin())
                    .senha(credenciaisDTO.getSenha())
                    .build();

            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);

            return new TokenDTO(usuario.getLogin(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, sw.toString());
        }
    }
}
