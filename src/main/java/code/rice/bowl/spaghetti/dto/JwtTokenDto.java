package code.rice.bowl.spaghetti.dto;


public record JwtTokenDto(String accessToken, String refreshToken) {
    public static JwtTokenDto of(String acToken, String reToken) {
        return new JwtTokenDto(acToken, reToken);
    }
}
