package com.future.tailormade.payload.request.user;

import com.future.tailormade.model.entity.user.Education;
import com.future.tailormade.model.entity.user.Occupation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserAdditionalInfoRequest {

    @NotBlank
    private String id;

    private Occupation occupation;

    private Education education;
}
