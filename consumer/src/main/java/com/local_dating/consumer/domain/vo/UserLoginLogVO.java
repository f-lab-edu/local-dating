package com.local_dating.consumer.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;
import java.time.LocalDateTime;

/*@Getter
@Setter
@NoArgsConstructor
public class UserLoginLogVO implements Serializable {

    private String userId;
    private String ip;
    private String lgFailYn;

    public UserLoginLogVO(String userId, String ip, String lgFailYn) {
        this.userId = userId;
        this.ip = ip;
        this.lgFailYn = lgFailYn;
    }
}*/

//public record UserLoginLogVO(@JsonProperty("userId") String userId, @JsonProperty("ip") String ip, @JsonProperty("lgFailYn") String lgFailYn) implements Serializable {
public record UserLoginLogVO(Long userId, String ip, String lgFailYn, LocalDateTime localDateTime) implements Serializable {
//public record UserLoginLogVO(String userId, String ip, String lgFailYn, LocalDateTime localDateTime) implements Serializable {
    @JsonCreator
    public UserLoginLogVO {
        // 기본 생성자
    }
}
