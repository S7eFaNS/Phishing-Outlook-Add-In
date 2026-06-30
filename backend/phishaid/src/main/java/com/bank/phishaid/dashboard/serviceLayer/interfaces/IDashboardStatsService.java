package com.bank.phishaid.dashboard.serviceLayer.interfaces;

import com.bank.phishaid.dashboard.dto.IpCountDto;
import com.bank.phishaid.dashboard.dto.SenderCountDto;
import com.bank.phishaid.dashboard.dto.SummaryDto;

import java.time.Instant;
import java.util.List;

public interface IDashboardStatsService {

    List<SenderCountDto> getTopSenders(int limit);

    List<IpCountDto> getTopSenderIps(int limit);

    SummaryDto getSummary(Instant from, Instant to);
}
