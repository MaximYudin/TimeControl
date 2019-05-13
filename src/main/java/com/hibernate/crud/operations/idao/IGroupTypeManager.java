package com.hibernate.crud.operations.idao;

import org.russianfeature.model.GroupType;
import org.russianfeature.model.Student;

import java.util.List;
import java.util.Map;

public interface IGroupTypeManager {

    GroupType findByGroupTypeName(String name);

    void deleteGroupType(GroupType student);

    List<GroupType> getAllGroupType();

    GroupType findGroupTypeById(int id);

    void saveGroupType(GroupType gt);

    void createGroupType(GroupType gt);

    List<GroupType> getDoubles(Map<String, String> params);

}
