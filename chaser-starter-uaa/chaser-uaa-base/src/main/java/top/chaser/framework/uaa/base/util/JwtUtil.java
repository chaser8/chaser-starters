package top.chaser.framework.uaa.base.util;

import cn.hutool.core.bean.BeanUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import top.chaser.framework.common.base.util.RsaUtil;
import top.chaser.framework.common.web.session.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author: chaser8
 * @date 2021/6/22 10:20 上午
 **/
@Slf4j
public class JwtUtil {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.RS256;

    public static User getUserDetailFromToken(String token, String publicKey) {
        final Claims claims = getClaimsFromToken(token, publicKey);
        if (claims == null) {
            return null;
        }
        User user = new User();
        return BeanUtil.fillBeanWithMap(claims, user, true);
    }

    private static Claims getClaimsFromToken(String token, String publicKey) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(RsaUtil.getPublicKey(publicKey))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("", e);
            throw new BadCredentialsException("Unauthorized access");
        }
        return claims;
    }


    private static Map<String, Object> generateClaims(User user) {
        Map<String, Object> claims = new HashMap<>(10);
        claims.put("userId", user.getUserId());
        claims.put("userCode", user.getUserCode());
        claims.put("nickname", user.getNickname());
        claims.put("roles", user.getRoles());
        claims.put("staffId", user.getStaffId());
        return claims;
    }

    public static String generateToken(User user, long expiration, String privateKey) {
        Map<String, Object> claims = generateClaims(user);
        String username = user.getUserCode();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                //暂不设置失效时间，在缓存中控制
//                .setExpiration(generateExpirationDate(expiration))
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SIGNATURE_ALGORITHM, RsaUtil.getPrivateKey(privateKey))
                .compact();
    }
}
