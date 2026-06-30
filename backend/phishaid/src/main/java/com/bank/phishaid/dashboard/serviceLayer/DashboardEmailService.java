package com.bank.phishaid.dashboard.serviceLayer;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.dashboard.dto.AttachmentDto;
import com.bank.phishaid.dashboard.dto.EmailDetailDto;
import com.bank.phishaid.dashboard.dto.EmailDto;
import com.bank.phishaid.dashboard.dto.LinkDto;
import com.bank.phishaid.dashboard.dto.RelayHopDto;
import com.bank.phishaid.dashboard.dto.ResultDto;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardEmailRepo;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardEmailService;
import com.bank.phishaid.initialization.entity.AttachmentLst;
import com.bank.phishaid.initialization.entity.LinkLst;
import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.MailRelayLst;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
class DashboardEmailService implements IDashboardEmailService {

    private final IDashboardEmailRepo repo;

    DashboardEmailService(IDashboardEmailRepo repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmailDto> getAllEmails(Pageable pageable) {
        return repo.findAll(pageable).map(DashboardEmailService::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailDto getEmailById(UUID phMailId) {
        return repo.findById(phMailId)
                .map(DashboardEmailService::toDto)
                .orElseThrow(() -> new NoSuchElementException("email " + phMailId + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public EmailDetailDto getEmailDetail(UUID phMailId) {
        // 404 if the email itself is unknown; the rest are assembled with separate, focused queries
        // (no cartesian mega-join) and tolerate absence.
        PhMail email = repo.findById(phMailId)
                .orElseThrow(() -> new NoSuchElementException("email " + phMailId + " not found"));

        MailPathLst mailPath = repo.findMailPathByPhMailId(phMailId).orElse(null);

        List<RelayHopDto> relay = repo.findRelayHopsByPhMailId(phMailId).stream()
                .map(DashboardEmailService::toRelayHopDto)
                .toList();
        List<LinkDto> links = repo.findLinksByPhMailId(phMailId).stream()
                .map(DashboardEmailService::toLinkDto)
                .toList();
        List<AttachmentDto> attachments = repo.findAttachmentsByPhMailId(phMailId).stream()
                .map(DashboardEmailService::toAttachmentDto)
                .toList();
        ResultDto result = repo.findResultByPhMailId(phMailId)
                .map(DashboardEmailService::toResultDto)
                .orElse(null);

        return new EmailDetailDto(
                email.getPhMailId(),
                email.getPhFrom(),
                email.getRcpt(),
                email.getSub(),
                email.getRepTo(),
                email.getRetPath(),
                email.getTimestampMail(),
                mailPath == null ? null : mailPath.getSenderIp(),
                mailPath == null ? null : mailPath.getMailSpf(),
                mailPath == null ? null : mailPath.getMailDkim(),
                mailPath == null ? null : mailPath.getMailDmarc(),
                relay,
                links,
                attachments,
                result);
    }

    private static EmailDto toDto(PhMail e) {
        return new EmailDto(
                e.getPhMailId(),
                e.getPhFrom(),
                e.getRcpt(),
                e.getSub(),
                e.getRepTo(),
                e.getRetPath(),
                e.getTimestampMail());
    }

    private static RelayHopDto toRelayHopDto(MailRelayLst e) {
        return new RelayHopDto(e.getHopNumber(), e.getHopDescription());
    }

    private static LinkDto toLinkDto(LinkLst e) {
        return new LinkDto(e.getLinkLstId(), e.getLinkUrl(), e.getCount());
    }

    private static AttachmentDto toAttachmentDto(AttachmentLst e) {
        return new AttachmentDto(e.getAttLstId(), e.getAttName(), e.getCount());
    }

    private static ResultDto toResultDto(AnalysRslt e) {
        return new ResultDto(
                e.getAnalysRsltId(),
                e.getPhMail().getPhMailId(),
                e.getTotalScore(),
                e.getAnalysDesc(),
                e.getFrwdToHost());
    }
}
