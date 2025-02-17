package by.gp.clinic.controller;

import by.gp.clinic.AbstractSpringMvcTest;
import by.gp.clinic.dbo.AbstractDbo;
import by.gp.clinic.dto.AbstractDto;
import by.gp.clinic.dto.PageDto;
import by.gp.clinic.repository.CustomRepository;
import by.gp.clinic.search.PageableSearchRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractControllerTest extends AbstractSpringMvcTest {

    private static final String SEARCH = "/search";
    private static final String ID = "id";

    @SuppressWarnings("UnusedReturnValue")
    Long addEntity() {
        return addEntity(getDtoMock());
    }

    void addEntityWithoutAnswer() {
        addEntityWithoutAnswer(getDtoMock(), "");
    }

    void addEntityWithoutAnswer(final Object dto, final String url) {
        final var result = postQuery(getUrl() + url, dto);
        assertEquals(200, result.getResponse().getStatus());
    }

    Long addEntity(final Object dto) {
        return addEntity(dto, getUrl());
    }

    Long addEntity(final Object dto, final String url) {
        final var result = postQuery(url, dto);

        final var answer = getJsonFormString(getContentAsString(result));
        System.out.println("answer = " + answer);
        final var id = getLongFromJson(answer, ID);

        assertNotNull(id);
        assertEquals(200, result.getResponse().getStatus());
        return id;
    }

    void addEntityWithStatus(final AbstractDto dto, final int status, final String errorMessage) {
        addEntityWithStatus(dto, status, errorMessage, getUrl());
    }

    void addEntityWithStatus(final Object dto,
                             final int status,
                             final String errorMessage, final String url) {
        final var result = postQuery(url, dto);
        assertEquals(status, result.getResponse().getStatus());
        final var answer = getObjectFromResult(getReplaced(result), HashMap.class);
        assertEquals(errorMessage, answer.get("message"));
    }

    void getEntityTest(Class<? extends AbstractDto> dtoClass, final Long id) {
        final var result = getQuery(getUrl() + "/" + id);
        final var savedEntity = getObjectFromResult(result, dtoClass);

        assertNotNull(savedEntity);
        assertEquals(200, result.getResponse().getStatus());
    }

    void removeEntityTest(final Long id) {
        final var result = deleteQuery(getUrl() + "/" + id);

        assertEquals(200, result.getResponse().getStatus());

        final Optional byId = getRepository().findById(id);
        assertFalse(byId.isPresent());
    }

    <N extends AbstractDto> void findEntitiesTest(final PageableSearchRequest searchRequest,
                                                  final TypeReference<PageDto<N>> typeReference) {
        var result = postQuery(getUrl() + SEARCH, searchRequest);
        final var list = getListOfObjectsFromResult(result, typeReference);
        assertNotNull(list);
    }

    private String getReplaced(final MvcResult result) {
        return getContentAsString(result).replace("[", "").replace("]", "");
    }

    protected abstract CustomRepository<? extends AbstractDbo, Long> getRepository();

    protected abstract AbstractDto getDtoMock();

    protected abstract String getUrl();
}
