package com.backend.domicare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.UserDTO.UserDTOBuilder;
import com.backend.domicare.dto.request.UpdateUserRequest;
import com.backend.domicare.model.User;
import com.backend.domicare.security.dto.RegisterResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User convertToUser(UserDTO userDTO);
    RegisterResponse convertToRegisterResponse(UserDTO user);
    UserDTO convertToUserDTO(UpdateUserRequest userRequest);


    default  UserDTO convertToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.address( user.getAddress() );
        userDTO.avatar( user.getAvatar() );
        userDTO.createAt( user.getCreateAt() );
        userDTO.createBy( user.getCreateBy() );
        userDTO.email( user.getEmail() );
        userDTO.isEmailConfirmed( user.isEmailConfirmed() );
        userDTO.emailConfirmationToken( user.getEmailConfirmationToken() );
        userDTO.id( user.getId() );
        userDTO.name( user.getName() );
        userDTO.password( user.getPassword() );
        userDTO.phone( user.getPhone() );
        userDTO.roles(RoleMapper.INSTANCE.convertToRoleDTOSet(user.getRoles()));
        userDTO.updateAt( user.getUpdateAt() );
        userDTO.updateBy( user.getUpdateBy() );

        return userDTO.build();
    }
}
