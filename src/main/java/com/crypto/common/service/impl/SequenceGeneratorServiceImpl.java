package com.crypto.common.service.impl;

import com.crypto.common.entity.SequenceGenerator;
import com.crypto.common.entity.SequenceType;
import com.crypto.common.repository.SequenceGeneratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SequenceGeneratorServiceImpl {

    @Autowired
    private SequenceGeneratorRepository sequenceGeneratorRepository;

    @Transactional
    public long getNextSequenceValue(SequenceType sequenceType) {
        SequenceGenerator sequenceGenerator = sequenceGeneratorRepository.findById(sequenceType.name())
                .orElseThrow(() -> new RuntimeException("Sequence type not found"));

        long nextValue = sequenceGenerator.getCurrentValue() + 1;
        sequenceGenerator.setCurrentValue(nextValue);
        sequenceGeneratorRepository.save(sequenceGenerator);

        return nextValue;
    }
}