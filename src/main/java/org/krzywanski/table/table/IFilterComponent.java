package org.krzywanski.table.table;

import java.awt.*;

public interface IFilterComponent {
    /**
     * @return  - value of filter
     */
    String getFilterValue();

    /**
     * @return - component to display
     */
    Component getComponent();

    /**
     * Clear filter
     */
    void clear();
}
