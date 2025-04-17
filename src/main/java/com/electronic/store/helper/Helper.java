package com.electronic.store.helper;

import com.electronic.store.dtos.PageableRespond;
import com.electronic.store.dtos.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Helper {

    public static <U,V>PageableRespond<V> getPageableResponse(Page<U> page, Class<V> classType) {
        List<U> entities = page.getContent();
        List<V> userDtos = entities.stream().map(object-> new ModelMapper().map(object,classType)).collect(Collectors.toList());


        // Create a pageable response object
        PageableRespond<V> respond=new PageableRespond<>();
        respond.setContent(userDtos);
        respond.setPageNumber(page.getNumber());
        respond.setPageSize(page.getSize());
        respond.setTotalElements(page.getTotalElements());
        respond.setTotalPages(page.getTotalPages());
        respond.setLastPage(page.isLast());

        return respond;
    }

}
