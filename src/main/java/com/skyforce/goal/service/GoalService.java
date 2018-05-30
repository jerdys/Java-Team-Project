package com.skyforce.goal.service;

import com.skyforce.goal.dto.GoalDto;
import com.skyforce.goal.model.Goal;
import com.skyforce.goal.model.Image;
import com.skyforce.goal.model.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface GoalService {
    List<Goal> findAll();

    Goal createGoal(GoalDto goalDto, Authentication authentication);

    List<Goal> findGoalsByUser(User user);

    Goal findGoalById(Long id);

    void save(Authentication authentication, GoalDto goalDto, Image image);
}
