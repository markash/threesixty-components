/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.markash.ui.component.menu;

import com.github.markash.ui.component.logo.Logo;
import com.github.markash.ui.component.notification.NotificationModel;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import kaesdingeling.hybridmenu.HybridMenu;
import kaesdingeling.hybridmenu.components.*;
import kaesdingeling.hybridmenu.data.MenuConfig;
import kaesdingeling.hybridmenu.data.interfaces.MenuComponent;
import kaesdingeling.hybridmenu.design.DesignItem;
import org.springframework.context.event.EventListener;

import java.util.Collection;
import java.util.Optional;

/**
 * <p>
 * This is a side bar component that has been especially designed to be used with the {@link com.vaadin.ui.themes.ValoTheme Valo} theme.
 * It is based on {@link com.vaadin.ui.CssLayout}s and the {@code MENU_} -styles.
 * </p>
 * <p>
 * The sections and items are declared using the {@link com.github.markash.ui.component.menu.annotation.MenuSection}
 * and {@link com.github.markash.ui.component.menu.annotation.MenuItem} annotations, respectively.
 * To use this side bar, simply enable it in your application configuration using
 * the {@link com.github.markash.ui.component.menu.annotation.EnableThreeSixtyMenu} annotation,
 * and inject it into your UI. Also remember to use the Valo theme.
 * </p>
 *
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class ThreeSixtyHybridMenu extends AbstractMenu {

    private HybridMenu hybridMenu;
    private ComponentFactory<MenuSectionDescriptor, HMSubMenu> sectionComponentFactory;
    private ComponentFactory<MenuItemDescriptor, MenuComponent<HMButton>> itemComponentFactory;
    private HybridTopMenuFactory topMenuFactory;
    private HybridSideMenuFactory sideMenuFactory;

    /**
     * You should not need to create instances of this component directly. Instead, just inject the side bar into
     * your UI.
     * @param utils the menu utils
     * @param logo the logo of the menu
     * @param navigationContent the navigation content container
     */
    public ThreeSixtyHybridMenu(
            final MenuUtils utils,
            final Logo logo,
            final Layout navigationContent) {

        super(utils);

        this.sectionComponentFactory = new HybridSectionComponentFactory();
        this.itemComponentFactory = new HybridItemComponentFactory();
        this.hybridMenu = HybridMenu.get()
                .withInitNavigator(false)
                .withNaviContent(navigationContent)
                .withConfig(MenuConfig.get().withDesignItem(DesignItem.getWhiteDesign()));

        this.setCompositionRoot(this.hybridMenu);
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(100, Unit.PERCENTAGE);

        this.topMenuFactory = new HybridTopMenuFactory(
                hybridMenu,
                getItemComponentFactory()
        );

        this.sideMenuFactory = new HybridSideMenuFactory(
                hybridMenu,
                logo,
                utils,
                getSectionComponentFactory(),
                getItemComponentFactory());
    }

    @Override
    public void attach() {
        /* Delay the build of the menu until UI is available */
        this.hybridMenu.build();
        /* Build the menu items */
        super.attach();
    }

    @Override
    protected Optional<TopMenuFactory> getTopMenuFactory() {

        return Optional.of(this.topMenuFactory);
    }

    @Override
    protected Optional<SideMenuFactory> getSideMenuFactory() {

        return Optional.of(this.sideMenuFactory);
    }

    private ComponentFactory<MenuSectionDescriptor, HMSubMenu> getSectionComponentFactory() {
        return this.sectionComponentFactory;
    }

    private ComponentFactory<MenuItemDescriptor, MenuComponent<HMButton>> getItemComponentFactory() {
        return this.itemComponentFactory;
    }

    public class HybridSectionComponentFactory implements ComponentFactory<MenuSectionDescriptor, HMSubMenu> {

        @Override
        public HMSubMenu createItemComponent(
                final MenuLocation menuLocation,
                final MenuSectionDescriptor descriptor) {

            return HMSubMenu.get()
                    .withCaption(descriptor.getCaption())
                    .withIcon(VaadinIcons.ABACUS)
                    ;
        }
    }

    @EventListener
    public void addNotification(
            final NotificationModel model) {

        Notification notification = new Notification()
                .withAutoRemove()
                .withCloseable()
                .withContent(model.getMessage())
                .withTitle(model.getTitle())
                .withIcon(model.getIcon())
                .withDisplayTime(model.getCreated());

        if (model.isRead()) {
            notification.makeAsReaded();
        }

        this.hybridMenu
                .getNotificationCenter()
                .add(notification);
    }

    public class HybridItemComponentFactory implements ComponentFactory<MenuItemDescriptor, MenuComponent<HMButton>> {

        @Override
        public MenuComponent<HMButton> createItemComponent(
                final MenuLocation menuLocation,
                final MenuItemDescriptor descriptor) {

            HMButton button;

            if (descriptor instanceof MenuItemDescriptor.ViewItemDescriptor) {

                button = HMButton.get()
                        .withIcon(descriptor.getIcon())
                        .withNavigateTo(((MenuItemDescriptor.ViewItemDescriptor) descriptor).getViewName());
            } else {

                button = HMButton.get()
                        .withIcon(descriptor.getIcon())
                        .withClickListener((Button.ClickEvent  event) -> descriptor.itemInvoked(getUI()));
            }

            switch (menuLocation) {
                case TOP:
                    button.withDescription(descriptor.getCaption());
                    break;
                case LEFT:
                    button.withCaption(descriptor.getCaption());
                    break;
            }

            return button;
        }
    }

    public class HybridTopMenuFactory implements TopMenuFactory {

        private final HybridMenu hybridMenu;
        private final ComponentFactory<MenuItemDescriptor, MenuComponent<HMButton>> itemComponentFactory;

        HybridTopMenuFactory(
                final HybridMenu hybridMenu,
                final ComponentFactory<MenuItemDescriptor, MenuComponent<HMButton>> itemComponentFactory) {

            this.hybridMenu = hybridMenu;
            this.itemComponentFactory = itemComponentFactory;
        }

        @Override
        public Component createTopMenu(
                final Collection<MenuItemDescriptor> descriptors) {

            TopMenu topMenu = hybridMenu.getTopMenu();

            topMenu.add(HMTextField.get(VaadinIcons.SEARCH, "Search ..."));

            descriptors
                    .stream()
                    .map(descriptor -> itemComponentFactory.createItemComponent(MenuLocation.TOP, descriptor))
                    .forEach(topMenu::add);


            hybridMenu.getNotificationCenter()
                    .setNotiButton(topMenu.add(HMButton.get()
                            .withDescription("Notifications")));

            return topMenu;
        }
    }

    public class HybridSideMenuFactory implements SideMenuFactory {

        private final Logo logo;
        private final HybridMenu hybridMenu;
        private final MenuUtils menuUtils;
        private final ComponentFactory<MenuSectionDescriptor, HMSubMenu> sectionComponentFactory;
        private final ComponentFactory<MenuItemDescriptor, MenuComponent<HMButton>> itemComponentFactory;

        HybridSideMenuFactory(
                final HybridMenu hybridMenu,
                final Logo logo,
                final MenuUtils menuUtils,
                final ComponentFactory<MenuSectionDescriptor, HMSubMenu> sectionComponentFactory,
                final ComponentFactory<MenuItemDescriptor, MenuComponent<HMButton>> itemComponentFactory) {

            this.logo = logo;
            this.menuUtils = menuUtils;
            this.hybridMenu = hybridMenu;
            this.sectionComponentFactory = sectionComponentFactory;
            this.itemComponentFactory = itemComponentFactory;
        }

        @Override
        public Component createSideMenu(
                final Collection<MenuSectionDescriptor> descriptors) {

            LeftMenu leftMenu = hybridMenu.getLeftMenu();
            leftMenu.add(HMLabel.get()
                    .withCaption(logo.getTitle())
                    .withIcon(new ThemeResource("images/logo.png")));

            HMSubMenu subMenu;
            for(MenuSectionDescriptor sectionDescriptor : descriptors) {

                if (sectionDescriptor.getCaption().equalsIgnoreCase("")) {
                    /* If section has no caption (i.e. default) then add menu items directly to menu */
                    for (MenuItemDescriptor itemDescriptor : menuUtils.getItems(sectionDescriptor)) {
                        leftMenu.add(itemComponentFactory.createItemComponent(MenuLocation.LEFT, itemDescriptor));
                    }
                } else {
                    /* If section has caption then add menu items to a new submenu */
                    subMenu = sectionComponentFactory.createItemComponent(MenuLocation.LEFT, sectionDescriptor);

                    for (MenuItemDescriptor itemDescriptor : menuUtils.getItems(sectionDescriptor)) {
                        subMenu.add(itemComponentFactory.createItemComponent(MenuLocation.LEFT, itemDescriptor));
                    }
                    leftMenu.add(subMenu);
                }
            }

            return leftMenu;
        }
    }
}
