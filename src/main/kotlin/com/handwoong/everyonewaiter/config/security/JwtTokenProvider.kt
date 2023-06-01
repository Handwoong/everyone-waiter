package com.handwoong.everyonewaiter.config.security

import com.handwoong.everyonewaiter.dto.member.TokenResponse
import com.handwoong.everyonewaiter.exception.ErrorCode.*
import com.handwoong.everyonewaiter.util.throwFail
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key

const val AUTH_CLAIM_KEY = "roles"

@Component
class JwtTokenProvider(
    @Value("\${JWT_SECRET}") private val secretKey: String,
) {

    final val key: Key

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateToken(authentication: Authentication): TokenResponse {
        val authority = authentication.authorities.map { it.authority }.first()

        val accessToken = Jwts.builder()
            .setSubject(authentication.name)
            .claim(AUTH_CLAIM_KEY, authority)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return TokenResponse.of(accessToken)
    }

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)
        val username = claims.subject
        val authority = claims[AUTH_CLAIM_KEY].toString()

        val authorities = mutableListOf<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(authority))

        val principal = User(username, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    fun validateToken(token: String): Boolean {
        return try {
            parseClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getClaims(token: String): Claims {
        return try {
            parseClaims(token).body
        } catch (e: Exception) {
            throwFail(IN_VALID_TOKEN)
        }
    }

    fun parseClaims(token: String): Jws<Claims> {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
    }

}
