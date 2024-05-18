package org.example.mssqlplay;

import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReportService {
    private final RptParentRepository rptParentRepository;
    private final RptChildRepository rptChildRepository;
    private final TransactionalOperator transactionalOperator;

    public ReportService(RptParentRepository rptParentRepository, RptChildRepository rptChildRepository, TransactionalOperator transactionalOperator) {
        this.rptParentRepository = rptParentRepository;
        this.rptChildRepository = rptChildRepository;
        this.transactionalOperator = transactionalOperator;
    }

    // Using repo::saveAll and not global transaction
    public Mono<Void> updateData(Reports reports) {
        return Flux.fromIterable(reports.reports)
                .flatMap(rptParentDto -> {
                    RptParent rptParent = new RptParent();
                    rptParent.setRptName(rptParentDto.name);
                    return rptParentRepository.save(rptParent)
                            .map(RptParent::getRptNr)
                            .map(rptNr -> rptParentDto.rptChildDtoList
                                    .stream()
                                    .map(rptChildDto -> {
                                        RptChild rptChild = new RptChild();
                                        rptChild.rptNr = rptNr;
                                        rptChild.pghName = rptChildDto.pghName;
                                        return rptChild;
                                    })
                                    .toList()
                            )
                            .flatMapMany(rptChildRepository::saveAll)
                            .then()
                            .as(transactionalOperator::transactional);
                }).then();
    }

}
