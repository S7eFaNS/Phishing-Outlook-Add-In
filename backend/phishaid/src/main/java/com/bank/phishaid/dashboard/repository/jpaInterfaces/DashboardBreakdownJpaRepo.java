package com.bank.phishaid.dashboard.repository.jpaInterfaces;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.analysis.entity.AttScoreLst;
import com.bank.phishaid.analysis.entity.HeaderScoreLst;
import com.bank.phishaid.analysis.entity.LinkScoreLst;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DashboardBreakdownJpaRepo extends JpaRepository<AnalysRslt, UUID> {

    // The 1:1 header provenance row.
    @Query("select h from HeaderScoreLst h where h.analysRslt.analysRsltId = :analysRsltId")
    Optional<HeaderScoreLst> findHeaderScoreByResultId(@Param("analysRsltId") UUID analysRsltId);

    // URL score rows for this result (linkScoreRL → linkScoreLst), each with its linkLst.
    @Query("select sc from LinkScoreRL rl join rl.linkScoreLst sc join fetch sc.linkLst "
            + "where rl.analysRslt.analysRsltId = :analysRsltId")
    List<LinkScoreLst> findLinkScoresByResultId(@Param("analysRsltId") UUID analysRsltId);

    // Attachment score rows for this result (attScoreRL → attScoreLst), each with its attLst.
    @Query("select sc from AttScoreRL rl join rl.attScoreLst sc join fetch sc.attLst "
            + "where rl.analysRslt.analysRsltId = :analysRsltId")
    List<AttScoreLst> findAttScoresByResultId(@Param("analysRsltId") UUID analysRsltId);
}
