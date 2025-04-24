package com.learning.grpc.userservice.service.handler;

import com.learning.grpc.userservice.entity.PortfolioItem;
import com.learning.grpc.userservice.entity.User;
import com.learning.grpc.userservice.exception.UnknownUserException;
import com.learning.grpc.userservice.repository.PortfolioItemRepository;
import com.learning.grpc.userservice.repository.UserRepository;
import com.learning.grpc.userservice.util.EntityMessageMapper;
import com.learning.user.UserInformation;
import com.learning.user.UserInformationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserInformationRequestHandler {

    private final UserRepository userRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public UserInformation getUserInformation(UserInformationRequest request) {
        User user = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UnknownUserException(request.getUserId()));

        List<PortfolioItem> portfolioItem = this.portfolioItemRepository
                .findAllByUserId(request.getUserId());

        return EntityMessageMapper.toUserInformation(user, portfolioItem);
    }
}
