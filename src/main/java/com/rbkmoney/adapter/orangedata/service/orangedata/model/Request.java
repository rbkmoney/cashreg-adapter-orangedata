package com.rbkmoney.adapter.orangedata.service.orangedata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.Group;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request {

    /**
     * Идентификатор документа
     * Строка от 1 до 32 символов
     */
    @JsonProperty("id")
    private String id;

    /**
     * ИНН организации, для которой пробивается чек
     * Строка 12 символов
     */
    @JsonProperty("inn")
    private String inn;

    /**
     * Группа устройств, с помощью которых будет пробит чек
     * Строка от 1 до 32 символов или null
     *
     * see {@link Group}
     */
    @JsonProperty("group")
    private String group = Group.DEFAULT;

    /**
     * Содержимое документа
     */
    @JsonProperty("content")
    private Content content;

    /**
     * Название ключа который должен быть использован для проверки подпись.
     * Опциональный параметр.
     * Если имя ключа не указано для проверки подписи будет использован ключ,
     * заданный по умолчанию.
     * Строка от 1 до 32 символов либо null
     */
    @JsonProperty("key")
    private String key;

}
