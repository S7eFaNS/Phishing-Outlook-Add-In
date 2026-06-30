package com.bank.phishaid.dashboard.repository.jpaInterfaces;

import com.bank.phishaid.dashboard.dto.IpCountDto;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface DashboardStatsJpaRepo extends JpaRepository<PhMail, UUID> {

    // Grouped fetch by the full From header; the service splits each to its domain and re-aggregates.
    @Query("select p.phFrom as phFrom, count(p) as count from PhMail p group by p.phFrom")
    List<SenderFromCount> findSenderCountsGroupedByFrom();

    // Sender IPs by recurrence; senderIp is a direct column so grouping/ordering/limit all run in SQL.
    // The limit comes from the Pageable; nulls are excluded (a missing IP is not a sender IP).
    @Query("select new com.bank.phishaid.dashboard.dto.IpCountDto(mp.senderIp, count(mp)) "
            + "from MailPathLst mp where mp.senderIp is not null "
            + "group by mp.senderIp order by count(mp) desc")
    List<IpCountDto> findTopSenderIps(Pageable pageable);

    // --- summary window (§13.3): scoped by the email's own timestampMail, [from, to] inclusive ---

    // Emails received in the window.
    @Query("select count(p) from PhMail p "
            + "where p.timestampMail >= :from and p.timestampMail <= :to")
    long countEmailsInWindow(@Param("from") Instant from, @Param("to") Instant to);

    // Results produced in the window, grouped by analysDesc; the service derives totalResults,
    // incompleteCount and the Level map from these rows.
    @Query("select r.analysDesc as analysDesc, count(r) as count from AnalysRslt r "
            + "where r.phMail.timestampMail >= :from and r.phMail.timestampMail <= :to "
            + "group by r.analysDesc")
    List<AnalysDescCount> findResultDescCountsInWindow(@Param("from") Instant from, @Param("to") Instant to);

    // Results in the window flagged for host forwarding (a header auth check failed).
    @Query("select count(r) from AnalysRslt r "
            + "where r.phMail.timestampMail >= :from and r.phMail.timestampMail <= :to "
            + "and r.frwdToHost = true")
    long countFrwdToHostInWindow(@Param("from") Instant from, @Param("to") Instant to);
}
