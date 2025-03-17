package com.backend.domicare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.model.User;
import com.backend.domicare.security.dto.RegisterResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO convertToUserDTO(User user);
    User convertToUser(UserDTO userDTO);
    RegisterResponse convertToRegisterResponse(UserDTO user);
}
