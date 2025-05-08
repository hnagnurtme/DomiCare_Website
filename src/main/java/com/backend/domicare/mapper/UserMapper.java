package com.backend.domicare.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.UserDTO.UserDTOBuilder;
import com.backend.domicare.dto.request.AddUserByAdminRequest;
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
        userDTO.id( user.getId() );
        userDTO.name( user.getName() );
        userDTO.password( user.getPassword() );
        userDTO.phone( user.getPhone() );
        userDTO.address( user.getAddress() );
        userDTO.avatar( user.getAvatar() );
        userDTO.email( user.getEmail() );
        userDTO.gender( user.getGender() );
        userDTO.dateOfBirth( user.getDateOfBirth() );
        userDTO.isEmailConfirmed( user.isEmailConfirmed() );
        userDTO.emailConfirmationToken( user.getEmailConfirmationToken() );
        userDTO.googleId( user.getGoogleId() );
        userDTO.roles(RoleMapper.INSTANCE.convertToRoleDTOSet(user.getRoles()));
        userDTO.updateAt( user.getUpdateAt() );
        userDTO.updateBy( user.getUpdateBy() );
        userDTO.createAt( user.getCreateAt() );
        userDTO.createBy( user.getCreateBy() );
        userDTO.isActive( user.isActive() );
        return userDTO.build();
    }

    default UserDTO convertToUserDTOByAddUserRequest(AddUserByAdminRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        UserDTOBuilder userDTO = UserDTO.builder();
        userDTO.name( userRequest.getName() );
        userDTO.password( userRequest.getPassword() );
        userDTO.phone( userRequest.getPhone() );
        userDTO.address( userRequest.getAddress() );
        userDTO.email( userRequest.getEmail() );

        return userDTO.build();
        
    }
    List<UserDTO> convertToUserDTOList(List<User> users);
}
