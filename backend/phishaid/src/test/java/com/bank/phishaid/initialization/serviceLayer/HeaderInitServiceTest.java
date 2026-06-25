package com.bank.phishaid.initialization.serviceLayer;

import com.bank.phishaid.initialization.dto.EmailHeaderDTO;
import com.bank.phishaid.initialization.dto.RelayHopDTO;
import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.MailRelayLst;
import com.bank.phishaid.initialization.entity.PhMail;
import com.bank.phishaid.initialization.repository.interfaces.IMailPathRepo;
import com.bank.phishaid.initialization.repository.interfaces.IMailRelayRepo;
import com.bank.phishaid.initialization.repository.interfaces.IPhMailRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HeaderInitServiceTest {

    @Mock private IPhMailRepo phMailRepo;
    @Mock private IMailPathRepo mailPathRepo;
    @Mock private IMailRelayRepo mailRelayRepo;

    @InjectMocks private HeaderInitService service;

    @Captor private ArgumentCaptor<PhMail> phMailCaptor;
    @Captor private ArgumentCaptor<MailPathLst> mailPathCaptor;
    @Captor private ArgumentCaptor<MailRelayLst> mailRelayCaptor;

    private EmailHeaderDTO head;

    @BeforeEach
    void setUp() {
        head = new EmailHeaderDTO();
        head.setPhFrom("\"PayPal Service\" <service@paypal-secure-verify.com>");
        head.setRcpt("victim@unicreditbulbank.bg");
        head.setSub("Urgent: Verify Your Account");
        head.setRepTo("<noreply@paypal-secure-verify.com>");
        head.setRetPath("<bounce@paypal-secure-verify.com>");
        head.setTimestampMail(Instant.parse("2025-06-16T08:15:23Z"));
        head.setSenderIp("198.51.100.23");
        head.setMailSpf(false);
        head.setMailDkim(false);
        head.setMailDmarc(false);
    }

    @Test
    void initPhMailMapsEveryHeaderFieldAndPersistsViaRepo() {
        when(phMailRepo.Create(any(PhMail.class))).thenAnswer(inv -> inv.getArgument(0));

        PhMail returned = service.InitPhMail(head);

        verify(phMailRepo).Create(phMailCaptor.capture());
        PhMail built = phMailCaptor.getValue();
        assertThat(built.getPhFrom()).isEqualTo(head.getPhFrom());
        assertThat(built.getRcpt()).isEqualTo(head.getRcpt());
        assertThat(built.getSub()).isEqualTo(head.getSub());
        assertThat(built.getRepTo()).isEqualTo(head.getRepTo());
        assertThat(built.getRetPath()).isEqualTo(head.getRetPath());
        assertThat(built.getTimestampMail()).isEqualTo(head.getTimestampMail());
        assertThat(returned).isSameAs(built);
    }

    @Test
    void initMailPathMapsPathFieldsAndLinksToPhMail() {
        PhMail parent = new PhMail();
        parent.setPhMailId(UUID.randomUUID());
        when(mailPathRepo.Create(any(MailPathLst.class))).thenAnswer(inv -> inv.getArgument(0));

        service.InitMailPath(head, parent);

        verify(mailPathRepo).Create(mailPathCaptor.capture());
        MailPathLst built = mailPathCaptor.getValue();
        assertThat(built.getPhMail()).isSameAs(parent);
        assertThat(built.getSenderIp()).isEqualTo("198.51.100.23");
        assertThat(built.getMailSpf()).isFalse();
        assertThat(built.getMailDkim()).isFalse();
        assertThat(built.getMailDmarc()).isFalse();
    }

    @Test
    void initMailRelayPersistsEachHopInOrderAndLinksItToTheMailPath() {
        head.setRelayChain(List.of(
                new RelayHopDTO(1, "from mx.unicreditbulbank.bg"),
                new RelayHopDTO(2, "from relay.example"),
                new RelayHopDTO(3, "from attacker-host.example [198.51.100.23]")));
        MailPathLst mailPath = new MailPathLst();
        when(mailRelayRepo.Create(any(MailRelayLst.class))).thenAnswer(inv -> inv.getArgument(0));

        List<MailRelayLst> persisted = service.InitMailRelay(head, mailPath);

        verify(mailRelayRepo, org.mockito.Mockito.times(3)).Create(mailRelayCaptor.capture());
        List<MailRelayLst> created = mailRelayCaptor.getAllValues();
        assertThat(created).extracting(MailRelayLst::getHopNumber).containsExactly(1, 2, 3);
        assertThat(created).extracting(MailRelayLst::getHopDescription)
                .containsExactly("from mx.unicreditbulbank.bg", "from relay.example",
                        "from attacker-host.example [198.51.100.23]");

        var order = inOrder(mailRelayRepo);
        for (MailRelayLst hop : created) {
            order.verify(mailRelayRepo).Create(hop);
            order.verify(mailRelayRepo).CreateJunctionColumn(mailPath, hop);
        }
        assertThat(persisted).hasSize(3);
    }

    @Test
    void initMailRelayDoesNothingWhenChainIsNull() {
        List<MailRelayLst> persisted = service.InitMailRelay(head, new MailPathLst());

        assertThat(persisted).isEmpty();
        verifyNoInteractions(mailRelayRepo);
    }
}
