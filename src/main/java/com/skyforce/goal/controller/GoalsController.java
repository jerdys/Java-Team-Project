package com.skyforce.goal.controller;

import com.skyforce.goal.model.Goal;
import com.skyforce.goal.model.User;
import com.skyforce.goal.service.AuthenticationService;
import com.skyforce.goal.service.GoalService;
import com.skyforce.goal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GoalsController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private GoalService goalService;

    @Autowired
    private UserService userService;

    @GetMapping("/goals")
    public String getGoalsPage(Authentication authentication, Model model) {
        model.addAttribute("user", authenticationService.getUserByAuthentication(authentication));
        model.addAttribute("goals", goalService.findAll());

        return "goals";
    }

    @GetMapping("goal/{id}")
    public String getGoalPage(Model model, @PathVariable("id") Long id) {
        Goal goal = goalService.findGoalById(id);
        model.addAttribute("goal", goal);
//        model.addAttribute("user", userService.findUserByLogin(login));
        return "goal";
    }
}
