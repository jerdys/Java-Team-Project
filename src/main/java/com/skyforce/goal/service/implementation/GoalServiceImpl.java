package com.skyforce.goal.service.implementation;

import com.skyforce.goal.dto.GoalDto;
import com.skyforce.goal.model.*;
import com.skyforce.goal.model.enums.MoneyDirection;
import com.skyforce.goal.repository.GoalRepository;
import com.skyforce.goal.repository.MoneyHistoryEntryRepository;
import com.skyforce.goal.model.enums.GoalState;
import com.skyforce.goal.service.AuthenticationService;
import com.skyforce.goal.service.GoalService;
import com.skyforce.goal.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final AuthenticationService authenticationService;
    private final MoneyHistoryEntryRepository moneyHistoryEntryRepository;
    private final ImageService imageService;

    @Autowired
    public GoalServiceImpl(GoalRepository goalRepository, AuthenticationService authenticationService,
                           MoneyHistoryEntryRepository moneyHistoryEntryRepository, ImageService imageService) {
        this.goalRepository = goalRepository;
        this.authenticationService = authenticationService;
        this.moneyHistoryEntryRepository = moneyHistoryEntryRepository;
        this.imageService = imageService;
    }

    @Override
    public List<Goal> findAll() {
        return goalRepository.findAll();
    }

    @Override
    public Goal createGoal(GoalDto goalDto, Authentication authentication) {
        User user = authenticationService.getUserByAuthentication(authentication);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Goal newGoal = Goal.builder()
                .user(user)
                .state(GoalState.INPROGRESS)
                .name(goalDto.getGoalName())
                .description(goalDto.getDescription())
                .dateStart(date)
                .dateEnd(goalDto.getDateEnd())
                .price(goalDto.getPrice())
                .build();

//        Checkpoint checkpoint = null;
//        while(goalDto.getCheckpointName()!=null){
        Checkpoint checkpoint = Checkpoint.builder()
                .name(goalDto.getCheckpointName())
                .description(goalDto.getDescription())
                .build();
//            checkpoint.setName(goalDto.getCheckpointName());
//            checkpoint.setDescription(goalDto.getCheckpointDescription());
        List<Checkpoint> checkpoints = new ArrayList<>();
        checkpoints.add(checkpoint);
        newGoal.setCheckpoints(checkpoints);

        goalRepository.save(newGoal);
        user.setMoney(user.getMoney().subtract(newGoal.getPrice()));
        MoneyHistoryEntry entry = MoneyHistoryEntry.builder()
                .user(user)
                .goal(newGoal)
                .direction(MoneyDirection.TO_MY_GOAL)
                .amount(newGoal.getPrice())
                .date(date)
                .build();

        System.out.println("THIS IS FROM DTO " + goalDto.getCheckpointName());
        System.out.println("THIS NAME IS FROM CHECKPOINT " + checkpoint.getName());
        return newGoal;
    }

    @Override
    public List<Goal> findGoalsByUser(User user) {
        return goalRepository.findGoalsByUser(user);
    }

    @Override
    public Goal findGoalById(Long id) {
        return goalRepository.findGoalById(id);
    }

    @Override
    public void save(Authentication authentication, GoalDto goalDto, Image image) {
        Goal goal = Goal.builder()
                .name(goalDto.getGoalName())
                .description(goalDto.getDescription())
                .accomplishmentCriteria(goalDto.getAccomplishmentCriteria())
                .image(image)
                .user(authenticationService.getUserByAuthentication(authentication))
                .dateStart(new Date())
                .build();

        goalRepository.save(goal);
    }


}
