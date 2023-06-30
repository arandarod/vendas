package br.aranda.service.impl;

import br.aranda.domain.entity.Usuario;
import br.aranda.domain.repository.UsuarioRepository;
import br.aranda.exception.SenhaInvalidaException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UserDetailsService {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base."));
        String[] roles = (usuario.isAdmin()) ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        return User.builder().username(usuario.getLogin()).password(usuario.getSenha()).roles(roles).build();
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        return repository.save(usuario);
    }

    public List<Usuario> getUsuarios() {
        return repository.findAll();
    }

    public UserDetails autenticar(Usuario usuario) {
        UserDetails user = loadUserByUsername(usuario.getLogin());
        boolean senhasBatem = encoder.matches(usuario.getSenha(), user.getPassword());

        if (senhasBatem) {
            return user;
        }

        throw new SenhaInvalidaException();
    }
}
