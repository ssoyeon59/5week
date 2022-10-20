package com.sparta.project.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {


    @NotBlank(message = "공백을 허용하지않습니다")
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "올바른 이메일형식이 아닙니다")
    private String email;

    @NotBlank(message = "공백을 허용하지않습니다")
    @Size(min = 4, max = 12, message = "4자 이상 12자이하로 입력해주세요")
    private String nickname;

    @NotBlank(message = "공백을 허용하지않습니다")
    @Size(min = 8, max = 20, message = "8자 이상 20자이하로 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$"
            , message = "비밀번호가 영어대소문자, 숫자, 특수문자를 모두 포함해주세요")
    private String password;

    @NotBlank
    private String passwordConfirm;
}

