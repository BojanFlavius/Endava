package com.example.carins.scheduler;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
public class InsuranceScheduler {

    private final InsurancePolicyRepository policyRepository;

    private final Set<Long> loggedPolicies = new HashSet<>();

    public InsuranceScheduler(InsurancePolicyRepository policyRepository){
        this.policyRepository = policyRepository;
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void logRecentlyExpiredPolices(){
        LocalDate now = LocalDate.now();

        List<InsurancePolicy> policies = policyRepository.findExpiredPolicies();

        for(InsurancePolicy policy:policies){
            if(!loggedPolicies.contains(policy.getId())){
                System.out.println("Policy " + policy.getId() + " for car " + policy.getCar().getId() + " expired on " + policy.getEndDate());

                loggedPolicies.add(policy.getId());
            }
        }
    }
}
