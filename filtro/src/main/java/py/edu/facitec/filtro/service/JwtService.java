package py.edu.facitec.filtro.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.entity.Usuario;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final String SECRET = "claveSuperSecretaMuyLargaQueTengaAlMenos32Caracteres!";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private final long EXPIRATION = 1000 * 60 * 60 * 24; // 24 horas

    public String generateToken(Usuario usuario) {
        // Obtener lista de roles del usuario
        List<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getTipoPersona().name())
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(usuario.getPersona().getNombre())
                .claim("id", usuario.getId())
                .claim("roles", roles) // Lista de roles SIN prefijo ROLE_
                .claim("authorities", roles) // También incluir como authorities
                .claim("email", usuario.getPersona().getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return getClaims(token).get("roles", List.class);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String subject = claims.getSubject();
        List<String> roles = getRolesFromToken(token);

        //CORRECCIÓN: Crear autoridades SIN prefijo ROLE_ para que coincida con el controller
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_"+rol)) // siempre hay que pasar con prefijo para que el token no sea nulo
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(subject, null, authorities);
    }
}