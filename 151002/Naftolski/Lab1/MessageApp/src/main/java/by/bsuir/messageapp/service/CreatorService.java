package by.bsuir.messageapp.service;

import by.bsuir.messageapp.dao.repository.CreatorRepository;
import by.bsuir.messageapp.model.request.CreatorRequestTo;
import by.bsuir.messageapp.model.response.CreatorResponseTo;
import by.bsuir.messageapp.service.exceptions.ResourceNotFoundException;
import by.bsuir.messageapp.service.exceptions.ResourceStateException;
import by.bsuir.messageapp.service.mapper.CreatorMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class CreatorService implements IService<CreatorRequestTo, CreatorResponseTo>{
    private final CreatorRepository creatorRepository;
    private final CreatorMapper creatorMapper;

    @Override
    public CreatorResponseTo findById(Long id) {
        return creatorRepository.getById(id).map(creatorMapper::getResponse).orElseThrow(() -> findByIdException(id));
    }

    @Override
    public List<CreatorResponseTo> findAll() {
        return creatorMapper.getListResponse(creatorRepository.getAll());
    }

    @Override
    public CreatorResponseTo create(CreatorRequestTo request) {
        return creatorRepository.save(creatorMapper.getCreator(request)).map(creatorMapper::getResponse).orElseThrow(CreatorService::createException);
    }

    @Override
    public CreatorResponseTo update(CreatorRequestTo request) {
        if (creatorMapper.getCreator(request).getId() == null)
        {
            throw findByIdException(creatorMapper.getCreator(request).getId());
        }

        return creatorRepository.update(creatorMapper.getCreator(request)).map(creatorMapper::getResponse).orElseThrow(CreatorService::updateException);
    }

    @Override
    public boolean removeById(Long id) {
        if (!creatorRepository.removeById(id)) {
            throw removeException();
        }
        return true;
    }

    private static ResourceNotFoundException findByIdException(Long id) {
        return new ResourceNotFoundException(HttpStatus.NOT_FOUND.value() * 100 + 11, "Can't find creator by id = " + id);
    }

    private static ResourceStateException createException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 12, "Can't create creator");
    }

    private static ResourceStateException updateException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 13, "Can't update creator");
    }

    private static ResourceStateException removeException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 14, "Can't remove creator");
    }
}