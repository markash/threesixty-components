package io.threesixty.ui.component.card;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.threesixty.ui.view.AbstractDashboardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

@SideBarItem(sectionId = Sections.DEFAULT, caption = DashboardView.TITLE)
@VaadinFontIcon(VaadinIcons.HOME)
@SpringView(name = DashboardView.VIEW_NAME)
public class DashboardView extends AbstractDashboardView {
	private static final long serialVersionUID = 1L;

    static final String TITLE = "Dashboard";
    static final String VIEW_NAME = "dashboard";
    static final VaadinIcons ICON = VaadinIcons.CALENDAR_O;

    private CssLayout dashboardPanels;

    @Autowired
    public DashboardView() {
        super(TITLE);
    }

    private Component buildEditButton() {
        Button result = new Button();
        //result.setId(EDIT_ID);
        result.setIcon(VaadinIcons.EDIT);
        result.addStyleName("icon-edit");
        result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        result.setDescription("Edit Dashboard");
        return result;
    }

    protected Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        
        Responsive.makeResponsive(dashboardPanels);

        dashboardPanels.addComponent(buildCard01());
        dashboardPanels.addComponent(buildCard02());
        dashboardPanels.addComponent(buildCard03());
        dashboardPanels.addComponent(buildCard04());
        dashboardPanels.addComponent(buildCard05());
        dashboardPanels.addComponent(buildCard06());
        return dashboardPanels;
    }

    private Component buildCard01() {
    	return new CounterStatisticsCard(
    			"Users",
    			VaadinIcons.USERS,
    			"The number of active users registered within the system.",
                () -> new CounterStatisticModel("StatA", 10),
    			""
        );
    }
    
    private Component buildCard02() {
    	return new CounterStatisticsCard(
    			"Stat A",
                VaadinIcons.CALENDAR_O,
    			"The number of stat A.",
                () -> new CounterStatisticModel("StatA", 12),
                ""
        );
    }

    private Component buildCard03() {
        return new CounterStatisticsCard(
                "Stat B",
                VaadinIcons.STAR_O,
                "The number of stat B.",
                () -> new CounterStatisticModel("StatB", 12),
                ""
        );
    }

    private Component buildCard04() {
        return new CounterStatisticsCard(
                "Stat C",
                VaadinIcons.CALENDAR_O,
                "The number of stat C.",
                () -> new CounterStatisticModel("StatC", 12),
                ""
        );
    }

    private Component buildCard05() {
        return new CounterStatisticsCard(
                "Stat D",
                VaadinIcons.BELL_O,
                "The number of stat D.",
                () -> new CounterStatisticModel("StatD", 12),
                ""
        );
    }

    private Component buildCard06() {
        return new CounterStatisticsCard(
                "Stat E",
                VaadinIcons.CART_O,
                "The number of stat E.",
                () -> new CounterStatisticModel("StatE", 12),
                ""
        );
    }


    @Override
    public void enter(final ViewChangeEvent event) {
    }

}

