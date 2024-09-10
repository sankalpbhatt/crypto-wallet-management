package com.crypto.common.service;

import com.crypto.common.entity.SequenceGenerator;
import com.crypto.common.entity.SequenceType;
import com.crypto.common.repository.SequenceGeneratorRepository;
import org.springframework.transaction.annotation.Transactional;

public class SequenceGeneratorServiceImpl {

    @Transactional
    public long getNextSequenceValue(SequenceType sequenceType,
                                     SequenceGeneratorRepository sequenceGeneratorRepository) {
        SequenceGenerator sequenceGenerator = sequenceGeneratorRepository.findBySequenceType(sequenceType.name())
                .orElseThrow(() -> new RuntimeException("Sequence type not found"));

        long nextValue = sequenceGenerator.getCurrentValue() + 1;
        sequenceGenerator.setCurrentValue(nextValue);
        sequenceGeneratorRepository.save(sequenceGenerator);

        return nextValue;
    }
}