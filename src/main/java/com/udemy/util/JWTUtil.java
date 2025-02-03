package com.udemy.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {
    private String secretKey = "aff9c8d268bca4587253fc37d111d559ef92ef2d613ce0a2dd8122f5fd68018a724ae78e044e231e6d24f56c09abcbf73ba863143d6dc9b2556921d1c078932e17eb9517ed60795b35c78fa712a0d016f6eeb0370046cd37bc28d2173919410977b7219bb07244228079ed784dab8aea830eeb790feb993ed6bcdb4530b7165b";
    private final String superRole = "ADMIN";

    public JWTUtil(){}

    /**
     * Genera un token JWT basado en un rol proporcionado.
     *
     * @param role el rol del usuario para incluir en el token.
     * @return el token JWT generado en formato compacto.
     */

    public String generateToken(String role) {
        // Diccionario de claims adicionales que se incluirán en el token.
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // Agrega el rol como un claim personalizado.

        return Jwts.builder()
                .setClaims(claims) // Establece los claims en el payload del token.
                .setSubject(role) // Define el subject del token (puede ser usado para almacenar identificadores).
                .setIssuedAt(new Date(System.currentTimeMillis())) // Establece la fecha y hora de emisión del token.
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // Establece la fecha de expiración del token (30 minutos).
                .signWith(getKey()) // Firma el token usando la clave secreta.
                .compact(); // Genera el token final en formato String.
    }

    /**
     * Decodifica la clave secreta Base64 y la convierte en una clave HMAC-SHA.
     *
     * @return la clave secreta en formato HMAC-SHA.
     */
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decodifica la clave secreta en bytes.
        return Keys.hmacShaKeyFor(keyBytes); // Genera la clave secreta a partir de los bytes decodificados.
    }

    /**
     * Extrae el rol (subject) de un token JWT.
     *
     * @param token el token JWT del que se extraerá el rol.
     * @return el rol extraído del token.
     */
    public String extractRole(String token) {
        return extractClaim(token, Claims::getSubject); // Llama a extractClaim para obtener el subject (rol).
    }

    /**
     * Verifica si el rol dentro del token coincide con el rol solicitado o si es un rol de superusuario.
     *
     * @param jwt el token JWT.
     * @param roleRequest el rol solicitado para comparar.
     * @return true si el rol coincide o si el rol es de superusuario; de lo contrario, false.
     */
    public boolean verifyRole(String jwt, String roleRequest) {
        String role = extractRole(jwt); // Extrae el rol del token.
        return role != null && (role.equals(roleRequest) || role.equals(superRole)); // Verifica si el rol coincide o es superusuario.
    }

    /**
     * Extrae un claim específico del token JWT utilizando una función de resolución.
     *
     * @param token el token JWT.
     * @param claimResolver una función para resolver el claim deseado.
     * @param <T> el tipo del claim a extraer.
     * @return el valor del claim extraído.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token); // Obtiene todos los claims del token.
        return claimResolver.apply(claims); // Aplica la función para resolver el claim específico.
    }

    /**
     * Extrae todos los claims (datos) de un token JWT.
     *
     * @param token el token JWT.
     * @return los claims extraídos del token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // Crea un parser para analizar el token.
                .setSigningKey(getKey()) // Configura la clave secreta para verificar la firma del token.
                .build() // Construye el parser.
                .parseClaimsJws(token) // Analiza y valida el token firmado.
                .getBody(); // Devuelve el cuerpo del token (claims).
    }

    /**
     * Verifica si un token ha expirado comparando su fecha de expiración con la fecha actual.
     *
     * @param token el token JWT.
     * @return true si el token ha expirado; de lo contrario, false.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Extrae la fecha de expiración y la compara con la fecha actual.
    }

    /**
     * Verifica si un token es válido (no expirado y correctamente firmado).
     *
     * @param token el token JWT.
     * @return true si el token es válido; de lo contrario, false.
     */
    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token); // El token es válido si no ha expirado.
        } catch (Exception e) {
            return false; // Si ocurre un error (firma inválida, formato incorrecto), el token no es válido.
        }
    }

    /**
     * Extrae la fecha de expiración del token.
     *
     * @param token el token JWT.
     * @return la fecha de expiración del token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Utiliza extractClaim para obtener la fecha de expiración.
    }
}