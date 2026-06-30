package com.bank.phishaid.dashboard.serviceLayer;

import com.bank.phishaid.analysis.model.Level;
import com.bank.phishaid.dashboard.dto.IpCountDto;
import com.bank.phishaid.dashboard.dto.SenderCountDto;
import com.bank.phishaid.dashboard.dto.SummaryDto;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardStatsRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.AnalysDescCount;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.SenderFromCount;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardStatsService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
class DashboardStatsService implements IDashboardStatsService {

    private final IDashboardStatsRepo repo;

    DashboardStatsService(IDashboardStatsRepo repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SenderCountDto> getTopSenders(int limit) {
        Map<String, Long> byDomain = new LinkedHashMap<>();
        for (SenderFromCount row : repo.findSenderCountsGroupedByFrom()) {
            String domain = senderDomain(row.getPhFrom());
            if (domain == null) {
                continue;
            }
            byDomain.merge(domain, row.getCount(), Long::sum);
        }

        return byDomain.entrySet().stream()
                .map(e -> new SenderCountDto(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(SenderCountDto::count).reversed())
                .limit(limit)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IpCountDto> getTopSenderIps(int limit) {
        return repo.findTopSenderIps(limit);
    }

    @Override
    @Transactional(readOnly = true)
    public SummaryDto getSummary(Instant from, Instant to) {
        long totalEmails = repo.countEmailsInWindow(from, to);
        long frwdToHostCount = repo.countFrwdToHostInWindow(from, to);

        Map<Level, Long> levels = new LinkedHashMap<>();
        for (Level level : Level.values()) {
            levels.put(level, 0L);
        }

        long totalResults = 0;
        long incompleteCount = 0;
        for (AnalysDescCount row : repo.findResultDescCountsInWindow(from, to)) {
            long count = row.getCount();
            totalResults += count;

            String desc = row.getAnalysDesc();
            if (isIncomplete(desc)) {
                incompleteCount += count;
            }
            Level level = parseLevel(desc);
            if (level != null) {
                levels.merge(level, count, Long::sum);
            }
        }

        return new SummaryDto(totalEmails, totalResults, levels, frwdToHostCount, incompleteCount, from, to);
    }

    private static boolean isIncomplete(String analysDesc) {
        return analysDesc != null && analysDesc.endsWith("(incomplete)");
    }

    private static Level parseLevel(String analysDesc) {
        if (analysDesc == null) {
            return null;
        }
        int suffix = analysDesc.indexOf("(incomplete)");
        String name = (suffix >= 0 ? analysDesc.substring(0, suffix) : analysDesc).trim();
        try {
            return Level.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String senderDomain(String phFrom) {
        if (phFrom == null) {
            return null;
        }
        int at = phFrom.lastIndexOf('@');
        if (at < 0 || at == phFrom.length() - 1) {
            return null;
        }
        String domain = phFrom.substring(at + 1).trim();
        int end = domain.length();
        for (int i = 0; i < domain.length(); i++) {
            char c = domain.charAt(i);
            if (c == '>' || c == ' ' || c == '\t') {
                end = i;
                break;
            }
        }
        domain = domain.substring(0, end);
        return domain.isEmpty() ? null : domain;
    }
}
