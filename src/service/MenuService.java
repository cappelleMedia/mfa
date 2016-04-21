package service;

import java.util.List;

public interface MenuService {

    List<MenuItem> getMenu();

    boolean isAuthenticationNeeded(String action);
}
