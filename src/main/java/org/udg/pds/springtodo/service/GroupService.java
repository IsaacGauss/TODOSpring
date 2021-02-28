package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.controller.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.Group;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.repository.GroupRepository;

import java.util.Collection;
import java.util.Optional;


@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserService userService;

    @Transactional
    public IdObject addGroup(String name, Long userId,String description) {
        try {
            User user = userService.getUser(userId);

            Group group = new Group(name,description);

            group.setOwner(user);

            user.addGroup(group);

            groupRepository.save(group);
            return new IdObject(group.getId());
        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }

    @Transactional
    public void addMember(Long userId, Long groupId, Long memberId){
        Group group=this.getGroup(groupId);
        if(group.getOwner().getId()!=userId)
            throw new ServiceException("This user is not the owner of the group");
        try{
            User user=userService.getUser(userId);
            User member=userService.getUser(memberId);
            group.addMember(member);
            member.addGroupMember(group);
        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }

    public Group getGroup(Long id) {
        Optional<Group> go = groupRepository.findById(id);
        if (go.isPresent())
            return go.get();
        else
            throw new ServiceException(String.format("Group with id = % dos not exists", id));
    }

    public Collection<Group> getOwnerGroups(Long id){return userService.getUser(id).getOwnerGroups();}

    public Collection<Group> getMemberGroups(Long id){return userService.getUser(id).getMemberGroups();}

    public Collection<User> getGroupMembers(Long userId,Long groupId){
        Group group=this.getGroup(groupId);
        if(group.getOwner().getId()!=userId && !group.getMembers().contains(userService.getUser(userId)))
            throw new ServiceException("This user is not the owner or a member of the group");
            return group.getMembers();
    }

}
