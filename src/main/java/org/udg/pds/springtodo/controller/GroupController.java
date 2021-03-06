package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.controller.exceptions.ControllerException;
import org.udg.pds.springtodo.entity.Group;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.service.GroupService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RequestMapping(path="/groups")
@RestController
public class GroupController extends BaseController{

    @Autowired
    GroupService groupService;

    @PostMapping(consumes = "application/json")
    public IdObject addGroup(HttpSession session, @Valid @RequestBody R_Group group) {

        Long userId = getLoggedUser(session);

        return groupService.addGroup(group.name, userId, group.description);
    }

    @GetMapping
    @JsonView(Views.Private.class)
    public Collection<Group> listAllGroups(HttpSession session) {
        Long userId = getLoggedUser(session);
        return groupService.getOwnerGroups(userId);
    }

    @PostMapping(path="/{gid}/members/{uid}")
    public String addMember(HttpSession session, @PathVariable("gid") Long groupId, @PathVariable("uid") Long memberId){
        Long userId=getLoggedUser(session);
        groupService.addMember(userId,groupId,memberId);
        return BaseController.OK_MESSAGE;
    }

    @GetMapping(path="/members")
    @JsonView(Views.Private.class)
    public Collection<Group> listAllMemberGroups(HttpSession session){
        Long userId = getLoggedUser(session);
        return groupService.getMemberGroups(userId);
    }

    @GetMapping(path="{gid}/members")
    @JsonView(Views.Private.class)
    public Collection<User> listAllMembers(HttpSession session, @PathVariable("gid") Long groupId){
        Long userId=getLoggedUser(session);
        return groupService.getGroupMembers(userId,groupId);
    }

    static class R_Group{

        @NotNull
        public String name;

        @NotNull
        public String description;
    }
}
