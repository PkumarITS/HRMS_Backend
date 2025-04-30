package com.phegondev.usersmanagementsystem.exceptions.useraccess;

import java.util.List;

public class RoleDeletionException extends RuntimeException {
    private final List<String> associatedActions;
    private final List<String> associatedCategories;
    private final List<String> associatedUsers;

    public RoleDeletionException(String message, List<String> associatedActions, 
                                  List<String> associatedCategories, List<String> associatedUsers) {
        super(message);
        this.associatedActions = associatedActions;
        this.associatedCategories = associatedCategories;
        this.associatedUsers = associatedUsers;
    }

    public List<String> getAssociatedActions() {
        return associatedActions;
    }

    public List<String> getAssociatedCategories() {
        return associatedCategories;
    }

    public List<String> getAssociatedUsers() {
        return associatedUsers;
    }
}

