package io.threesixty.ui.component.card;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import kaesdingeling.hybridmenu.HybridMenu;
import kaesdingeling.hybridmenu.builder.HybridMenuBuilder;
import kaesdingeling.hybridmenu.builder.left.LeftMenuButtonBuilder;
import kaesdingeling.hybridmenu.builder.left.LeftMenuSubMenuBuilder;
import kaesdingeling.hybridmenu.builder.top.TopMenuLabelBuilder;
import kaesdingeling.hybridmenu.data.MenuConfig;
import kaesdingeling.hybridmenu.data.enums.EMenuComponents;
import kaesdingeling.hybridmenu.data.enums.EMenuStyle;
import kaesdingeling.hybridmenu.data.leftmenu.MenuButton;
import kaesdingeling.hybridmenu.data.leftmenu.MenuSubMenu;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.SideBarSectionDescriptor;
import org.vaadin.spring.sidebar.SideBarUtils;
import org.vaadin.spring.sidebar.components.AbstractSideBar;
import org.vaadin.spring.sidebar.components.ValoSideBar;

import java.util.Collection;

public class HybridMenuSideBar extends AbstractSideBar<HybridMenu> {

    private HybridMenu menu;

    /**
     * You should not need to create instances of this component directly. Instead, just inject the side bar into
     * your UI.
     */
    public HybridMenuSideBar(
            final SideBarUtils sideBarUtils,
            final MenuConfig menuConfig,
            final io.threesixty.ui.component.card.ApplicationTitle applicationTitle) {
        super(sideBarUtils);

        this.menu = HybridMenuBuilder.get()
                .setContent(new VerticalLayout())
                .setMenuComponent(EMenuComponents.LEFT_WITH_TOP)
                .setConfig(menuConfig)
                .build();

        TopMenuLabelBuilder.get()
                .setCaption(applicationTitle.getTitle())
                .addStyleName(EMenuStyle.LABEL_TITLE)
                .setIcon(new ThemeResource("images/hybridmenu-Logo.png"))
                .build(this.menu);
    }

    public HybridMenu getMenu() {
        return menu;
    }

    @Override
    protected HybridMenu createCompositionRoot() {
        return this.menu;
    }

    /**
     * Returns whether the side bar is using large icons or not. Default is false.
     *
     * @see ValoTheme#MENU_PART_LARGE_ICONS
     */
    public boolean isLargeIcons() {
        return false;
    }

    /**
     * Specifies whether the side bar should use large icons or not.
     *
     * @see ValoTheme#MENU_PART_LARGE_ICONS
     */
    public void setLargeIcons(boolean largeIcons) {
    }

    /**
     * Adds a header to the top of the side bar, below the logo. The {@link ValoTheme#MENU_TITLE} style
     * will automatically be added to the layout.
     *
     * @param headerLayout the layout containing the header, or {@code null} to remove.
     */
    public void setHeader(Layout headerLayout) {

    }

    /**
     * Returns the header layout, or {@code null} if none has been set.
     *
     * @see #setHeader(com.vaadin.ui.Layout)
     */
    public Layout getHeader() {
        return null;
    }

    @Override
    protected SectionComponentFactory<HybridMenu> createDefaultSectionComponentFactory() {
        return new HybridMenuSideBar.DefaultSectionComponentFactory();
    }

    @Override
    protected ItemComponentFactory createDefaultItemComponentFactory() {
        return new HybridMenuSideBar.DefaultItemComponentFactory();
    }

    /**
     * Default implementation of {@link ValoSideBar.SectionComponentFactory} that adds the section header
     * and items directly to the composition root.
     */
    public class DefaultSectionComponentFactory implements SectionComponentFactory<HybridMenu> {

        private ItemComponentFactory itemComponentFactory;

        @Override
        public void setItemComponentFactory(ItemComponentFactory itemComponentFactory) {
            this.itemComponentFactory = itemComponentFactory;
        }

        @Override
        public void createSection(final HybridMenu menu, SideBarSectionDescriptor descriptor, Collection<SideBarItemDescriptor> itemDescriptors) {

            MenuSubMenu section = LeftMenuSubMenuBuilder.get()
                    .setCaption(descriptor.getCaption())
                    .setIcon(VaadinIcons.INFO_CIRCLE)
                    .setConfig(menu.getConfig())
                    .build(menu);

            for (SideBarItemDescriptor item : itemDescriptors) {
                section.addLeftMenuButton((MenuButton) itemComponentFactory.createItemComponent(item));
            }
        }
    }

    /**
     * Default implementation of {@link ValoSideBar.ItemComponentFactory} that creates
     * {@link com.vaadin.ui.Button}s.
     */
    public class DefaultItemComponentFactory implements ItemComponentFactory {

        @Override
        public Component createItemComponent(final SideBarItemDescriptor descriptor) {
            if (descriptor instanceof SideBarItemDescriptor.ViewItemDescriptor) {

                SideBarItemDescriptor.ViewItemDescriptor d = (SideBarItemDescriptor.ViewItemDescriptor) descriptor;
                return LeftMenuButtonBuilder.get()
                        .setCaption(d.getCaption())
                        .setIcon(d.getIcon())
                        .navigateTo(d.getViewName())
                        .build();
            } else {

                MenuButton button = LeftMenuButtonBuilder.get()
                        .setCaption(descriptor.getCaption())
                        .setIcon(descriptor.getIcon())
                        .build();
                button.addClickListener(new Button.ClickListener() {
                    private static final long serialVersionUID = -8512905888847432801L;
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        try {
                            descriptor.itemInvoked(getUI());
                        } finally {
                            setEnabled(true);
                        }
                    }
                });

                return button;
            }
        }
    }
}
