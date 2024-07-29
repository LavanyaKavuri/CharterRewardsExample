package org.example.service;

public class RewardsServiceImpl {

    Timestamp lastMonthTimestamp = getDateBasedOnOffSetDays(Constants.daysInMonths);
    Timestamp lastSecondMonthTimestamp = getDateBasedOnOffSetDays(2*Constants.daysInMonths);
    Timestamp lastThirdMonthTimestamp = getDateBasedOnOffSetDays(3*Constants.daysInMonths);

    List<Transaction> lastMonthTransactions = transactionRepository.findAllByCustomerIdAndTransactionDateBetween(
            customerId, lastMonthTimestamp, Timestamp.from(Instant.now()));
    List<Transaction> lastSecondMonthTransactions = transactionRepository
            .findAllByCustomerIdAndTransactionDateBetween(customerId, lastSecondMonthTimestamp, lastMonthTimestamp);
    List<Transaction> lastThirdMonthTransactions = transactionRepository
            .findAllByCustomerIdAndTransactionDateBetween(customerId, lastThirdMonthTimestamp,
                    lastSecondMonthTimestamp);

    Long lastMonthRewardPoints = getRewardsPerMonth(lastMonthTransactions);
    Long lastSecondMonthRewardPoints = getRewardsPerMonth(lastSecondMonthTransactions);
    Long lastThirdMonthRewardPoints = getRewardsPerMonth(lastThirdMonthTransactions);

    Rewards customerRewards = new Rewards();
		customerRewards.setCustomerId(customerId);
		customerRewards.setLastMonthRewardPoints(lastMonthRewardPoints);
		customerRewards.setLastSecondMonthRewardPoints(lastSecondMonthRewardPoints);
		customerRewards.setLastThirdMonthRewardPoints(lastThirdMonthRewardPoints);
		customerRewards.setTotalRewards(lastMonthRewardPoints + lastSecondMonthRewardPoints + lastThirdMonthRewardPoints);

		return customerRewards;

}

private Long getRewardsPerMonth(List<Transaction> transactions) {
    return transactions.stream().map(transaction -> calculateRewards(transaction))
            .collect(Collectors.summingLong(r -> r.longValue()));
}

private Long calculateRewards(Transaction t) {
    if (t.getTransactionAmount() > Constants.firstRewardLimit && t.getTransactionAmount() <= Constants.secondRewardLimit) {
        return Math.round(t.getTransactionAmount() - Constants.firstRewardLimit);
    } else if (t.getTransactionAmount() > Constants.secondRewardLimit) {
        return Math.round(t.getTransactionAmount() - Constants.secondRewardLimit) * 2
                + (Constants.secondRewardLimit - Constants.firstRewardLimit);
    } else
        return 0l;

}

public Timestamp getDateBasedOnOffSetDays(int days) {
    return Timestamp.valueOf(LocalDateTime.now().minusDays(days));
}
}
