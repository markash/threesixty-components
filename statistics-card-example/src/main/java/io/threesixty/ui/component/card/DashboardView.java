package io.threesixty.ui.component.card;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.threesixty.ui.component.chart.options.Axis;
import io.threesixty.ui.component.chart.options.DataPoint;
import io.threesixty.ui.view.AbstractDashboardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

@SideBarItem(sectionId = Sections.DEFAULT, caption = DashboardView.TITLE)
@VaadinFontIcon(VaadinIcons.HOME)
@SpringView(name = DashboardView.VIEW_NAME)
public class DashboardView extends AbstractDashboardView {
	private static final long serialVersionUID = 1L;

    static final String TITLE = "Dashboard";
    static final String VIEW_NAME = "dashboard";
    static final VaadinIcons ICON = VaadinIcons.CALENDAR_O;

    private CssLayout dashboardPanels;

    private CounterStatisticsCard tradesCard = new CounterStatisticsCard(VaadinIcons.STOCK, "");
    private CounterStatisticsCard usersCard = new CounterStatisticsCard(VaadinIcons.USERS, "");
    private CounterStatisticsCard hourlyTradesCard = new CounterStatisticsCard(VaadinIcons.TIMER, "");
    private Binder<Statistics> binder = new Binder<>(Statistics.class);
    private Statistics statistics = new Statistics();

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

        hourlyTradesCard.withHourlyInterval(Axis.X, 3);

        this.binder.bind(usersCard, Statistics::getUsers, Statistics::setUsers);
        this.binder.bind(tradesCard, Statistics::getSales, Statistics::setSales);
        this.binder.bind(hourlyTradesCard, Statistics::getHourlyTrades, Statistics::setHourlyTrades);
        this.binder.setBean(this.statistics);



//        dashboardPanels.addComponent(buildCard01());
//        dashboardPanels.addComponent(buildCard02());
//        dashboardPanels.addComponent(buildCard03());
//        dashboardPanels.addComponent(buildCard04());
//        dashboardPanels.addComponent(buildCard05());
//        dashboardPanels.addComponent(buildCard06());
        dashboardPanels.addComponent(usersCard);
        dashboardPanels.addComponent(tradesCard);
        dashboardPanels.addComponent(hourlyTradesCard);

        return dashboardPanels;
    }

//    private Component buildCard01() {
//    	return new CounterStatisticsCard(
//    			"Users",
//    			VaadinIcons.USERS,
//    			"The number of active users registered within the system.",
//                () -> new CounterStatisticModel("StatA", 10),
//    			""
//        );
//    }
//
//    private Component buildCard02() {
//    	return new CounterStatisticsCard(
//    			"Stat A",
//                VaadinIcons.CALENDAR_O,
//    			"The number of stat A.",
//                () -> new CounterStatisticModel("StatA", 12),
//                ""
//        );
//    }
//
//    private Component buildCard03() {
//        return new CounterStatisticsCard(
//                "Stat B",
//                VaadinIcons.STAR_O,
//                "The number of stat B.",
//                () -> new CounterStatisticModel("StatB", 12),
//                ""
//        );
//    }
//
//    private Component buildCard04() {
//        return new CounterStatisticsCard(
//                "Stat C",
//                VaadinIcons.CALENDAR_O,
//                "The number of stat C.",
//                () -> new CounterStatisticModel("StatC", 12),
//                ""
//        );
//    }
//
//    private Component buildCard05() {
//        return new CounterStatisticsCard(
//                "Stat D",
//                VaadinIcons.BELL_O,
//                "The number of stat D.",
//                () -> new CounterStatisticModel("StatD", 12.10, CounterStatisticModel.CounterFormat.PERCENTAGE),
//                ""
//        );
//    }
//
//    private Component buildCard06() {
//        return new CounterStatisticsCard(
//                "Stat E",
//                VaadinIcons.CART_O,
//                "The number of stat E.",
//                () -> new CounterStatisticModel("StatE", 120345.10),
//                ""
//        );
//    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    public static class Statistics implements Serializable {
        static String FIELD_SALES = "sales";

        static double[] Sales =
                new Random()
                        .doubles(50, 20020.00, 90000.00)
                        .toArray();

        static int[] Users =
                new Random()
                        .ints(50, 1, 1500)
                        .toArray();

        static  DataPoint[] HourlyTrades = new DataPoint[] {

                new DataPoint(12500, LocalDateTime.of(2017,12,7,8,0)),
                new DataPoint(9800, LocalDateTime.of(2017,12,7,9,0)),
                new DataPoint(3400, LocalDateTime.of(2017,12,7,10,0)),
                new DataPoint(1687, LocalDateTime.of(2017,12,7,11,0)),
                new DataPoint(1898, LocalDateTime.of(2017,12,7,12,0)),
                new DataPoint(2988, LocalDateTime.of(2017,12,7,13,0)),
                new DataPoint(5788, LocalDateTime.of(2017,12,7,14,0)),
                new DataPoint(7772, LocalDateTime.of(2017,12,7,15,0)),
                new DataPoint(10292, LocalDateTime.of(2017,12,7,16,0))
        };

        private CounterStatisticModel sales =
                new CounterStatisticModel(
                        "Trades",
                        DataLabelSettings.CURRENCY(),
                        DoubleStream.of(Sales)
                                .boxed()
                                .map(n -> new BigDecimal(n).setScale(2, RoundingMode.HALF_UP).doubleValue())
                                .map(DataPoint::new)
                                .collect(Collectors.toList())
                );

        private CounterStatisticModel users =
                new CounterStatisticModel(
                        "Registered Users",
                        DataLabelSettings.INTEGER(),
                        IntStream.of(Users)
                                .boxed()
                                .map(DataPoint::new)
                                .collect(Collectors.toList()))
                        .withShow(StatisticShow.Last)
                        .withStatFollowedByCategory();


        private CounterStatisticModel hourlyTrades =
                new CounterStatisticModel(
                        "Hourly Trade Volume",
                        DataLabelSettings.INTEGER(),
                        Arrays.stream(HourlyTrades)
                                .collect(Collectors.toList()))
                        .withShow(StatisticShow.Last)
                        .withStartingPoint(HourlyTrades[0])
                        .withPointInterval(24 * 3600 * 1000)
                        .withIconHidden()
                        .withCategoryFollowedByStat()
                ;

        public CounterStatisticModel getSales() { return sales; }
        public void setSales(final CounterStatisticModel sales) { this.sales = sales; }

        public CounterStatisticModel getUsers() { return users; }
        public void setUsers(CounterStatisticModel users) { this.users = users; }

        public CounterStatisticModel getHourlyTrades() { return hourlyTrades; }
        public void setHourlyTrades(CounterStatisticModel hourlyTrades) { this.hourlyTrades = hourlyTrades; }
    }
}

