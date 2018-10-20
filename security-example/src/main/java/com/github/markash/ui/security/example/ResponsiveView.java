package com.github.markash.ui.security.example;

import com.github.markash.ui.component.card.*;
import com.github.markash.ui.component.chart.options.Axis;
import com.github.markash.ui.component.chart.options.DataPoint;
import com.github.markash.ui.component.field.HeaderToolbar;
import com.github.markash.ui.component.field.Toolbar;
import com.github.markash.ui.component.menu.annotation.MenuItem;
import com.github.markash.ui.component.menu.annotation.VaadinFontIcon;
import com.github.markash.ui.view.AbstractDashboardView;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;

import javax.tools.Tool;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

@MenuItem(sectionId = Sections.DEFAULT, caption = ResponsiveView.TITLE, order = 3, topMenu = true)
@VaadinFontIcon(VaadinIcons.TIMER)
@SpringView(name = ResponsiveView.VIEW_NAME)
public class ResponsiveView extends AbstractDashboardView {
	private static final long serialVersionUID = 1L;

    static final String TITLE = "Response";
    static final String VIEW_NAME = "responsive";

    private CounterStatisticsCard tradesCard = new CounterStatisticsCard(VaadinIcons.STOCK, "");
    private CounterStatisticsCard usersCard = new CounterStatisticsCard(VaadinIcons.USERS, "");
    private CounterStatisticsCard hourlyTradesCard = new CounterStatisticsCard(VaadinIcons.TIMER, "");
    private CounterStatisticsCard tradersCountCard = new CounterStatisticsCard(VaadinIcons.USER, "");

    private Binder<Statistics> binder = new Binder<>(Statistics.class);
    private Statistics statistics = new Statistics();

    @Autowired
    public ResponsiveView(
            final Toolbar toolbar) {

        super(TITLE, toolbar);

        setShowToolbar(false);
    }

    protected Component buildContent() {

        MButton refreshButton = new MButton(VaadinIcons.REFRESH, "Refresh", event -> {
            this.statistics.incrementTraders();
            this.tradersCountCard.refresh();
            this.hourlyTradesCard.refresh();
        });

        getToolbar().setCaption(getTitle());
        getToolbar().removeAll();
        getToolbar().add(refreshButton, HeaderToolbar.ToolbarSection.ACTION);

        this.hourlyTradesCard.withHourlyInterval(Axis.X, 3);
        this.binder.bind(usersCard, Statistics::getUsers, Statistics::setUsers);
        this.binder.bind(tradesCard, Statistics::getSales, Statistics::setSales);
        this.binder.bind(hourlyTradesCard, Statistics::getHourlyTrades, Statistics::setHourlyTrades);
        this.binder.bind(tradersCountCard, Statistics::getTradersCount, Statistics::setTradersCount);
        this.binder.setBean(this.statistics);

        ResponsiveLayout responsiveLayout = new ResponsiveLayout().withDefaultRules(12, 6, 4, 3).withFlexible();
        ResponsiveRow row = responsiveLayout.addRow().withGrow(true).withSpacing(true);
        row.addColumn().withComponent(usersCard);
        row.addColumn().withComponent(tradesCard);
        row.addColumn().withComponent(hourlyTradesCard);
        row.addColumn().withComponent(tradersCountCard);

        return responsiveLayout;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    public static class Statistics implements Serializable {

        static AtomicLong traders = new AtomicLong(52L);

        static double[] Sales =
                new Random()
                        .doubles(50, 20020.00, 90000.00)
                        .toArray();

        static int[] Users =
                new Random()
                        .ints(50, 1, 1500)
                        .toArray();

        private List<DataPoint> hourlyTradeValues = new ArrayList<>(Arrays.asList(
                new DataPoint(12500, LocalDateTime.of(2017,12,7,8,0)),
                new DataPoint(9800, LocalDateTime.of(2017,12,7,9,0)),
                new DataPoint(3400, LocalDateTime.of(2017,12,7,10,0)),
                new DataPoint(1687, LocalDateTime.of(2017,12,7,11,0)),
                new DataPoint(1898, LocalDateTime.of(2017,12,7,12,0)),
                new DataPoint(2988, LocalDateTime.of(2017,12,7,13,0)),
                new DataPoint(5788, LocalDateTime.of(2017,12,7,14,0)),
                new DataPoint(7772, LocalDateTime.of(2017,12,7,15,0)),
                new DataPoint(10292, LocalDateTime.of(2017,12,7,16,0))
        ));

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
                        hourlyTradeValues)
                        .withShow(StatisticShow.Last)
                        .withStartingPoint(hourlyTradeValues.get(0))
                        .withPointInterval(PointInterval.HOURLY)
                        .withIconHidden()
                        .withCategoryFollowedByStat()
                ;

        CounterStatisticModel tradersCount =
                new CounterStatisticModel("Traders", traders.get())
                        .withShow(StatisticShow.Sum)
                        .withIconHidden()
                        .withShowOnlyStatistic(true);

        CounterStatisticModel getTradersCount() { return tradersCount; }
        void setTradersCount(final CounterStatisticModel tradersCount) { this.tradersCount = tradersCount; }

        CounterStatisticModel getSales() { return sales; }
        void setSales(final CounterStatisticModel sales) { this.sales = sales; }

        CounterStatisticModel getUsers() { return users; }
        void setUsers(CounterStatisticModel users) { this.users = users; }

        CounterStatisticModel getHourlyTrades() { return hourlyTrades; }
        void setHourlyTrades(CounterStatisticModel hourlyTrades) { this.hourlyTrades = hourlyTrades; }

        public void incrementTraders() {
            getTradersCount().setValue(traders.incrementAndGet());

            Supplier<LocalDateTime> dateSupplier = () -> {
                List<DataPoint> values = hourlyTrades.getValues();
                Number epochSeconds = values.get(values.size() - 1).getX();
                LocalDateTime lastDate = LocalDateTime.ofEpochSecond(epochSeconds.longValue() / 1000, 0, ZoneOffset.UTC);
                return lastDate.plusHours(1);
            };

            new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                    .longs(10,1898, 45772)
                    .mapToObj(value -> new DataPoint(value, dateSupplier.get()))
                    .forEach(hourlyTrades::addValue);
        }
    }
}

