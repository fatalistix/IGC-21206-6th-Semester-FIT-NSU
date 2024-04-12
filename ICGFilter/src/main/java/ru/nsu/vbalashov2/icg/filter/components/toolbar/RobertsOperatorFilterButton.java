package ru.nsu.vbalashov2.icg.filter.components.toolbar;

import io.reactivex.rxjava3.subjects.PublishSubject;
import ru.nsu.vbalashov2.icg.filter.components.options.robertsoperatorfilter.RobertsOperatorFilterOptionsPanel;
import ru.nsu.vbalashov2.icg.filter.tools.filters.FilterType;

import javax.swing.*;

public class RobertsOperatorFilterButton extends JToggleButton {

    private int initialThreshold = 128;
    private int resultThreshold = 128;

    public RobertsOperatorFilterButton(
            Icon icon,
            PublishSubject<Integer> robertsOperatorNewThresholdPublishSubject,
            PublishSubject<FilterType> newFilterTypePublishSubject
    ) {
        super(icon);

        setToolTipText("Highlight contours (Roberts operator)");

        newFilterTypePublishSubject.subscribe(newFilter -> setSelected(newFilter == FilterType.ROBERTS_OPERATOR));
        robertsOperatorNewThresholdPublishSubject.subscribe(newValue -> initialThreshold = newValue);

        PublishSubject<Integer> dynamicThresholdChangingPublishSubject = PublishSubject.create();

        dynamicThresholdChangingPublishSubject.subscribe(value -> resultThreshold = value);

        RobertsOperatorFilterOptionsPanel optionsPanel = new RobertsOperatorFilterOptionsPanel(
                dynamicThresholdChangingPublishSubject
        );

        addActionListener(event -> {
            dynamicThresholdChangingPublishSubject.onNext(initialThreshold);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    optionsPanel,
                    "Roberts operator settings",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (result == JOptionPane.OK_OPTION) {
                initialThreshold = resultThreshold;
                robertsOperatorNewThresholdPublishSubject.onNext(resultThreshold);
                newFilterTypePublishSubject.onNext(FilterType.ROBERTS_OPERATOR);
            }
        });
    }
}
