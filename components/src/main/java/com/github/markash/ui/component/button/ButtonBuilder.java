package com.github.markash.ui.component.button;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.github.markash.ui.I8n;
import com.github.markash.ui.Style;
import org.apache.commons.lang3.StringUtils;

public class ButtonBuilder {
	
	private ButtonBuilder() {}
	
	public static Button build(final String caption, final ClickListener listener) {
		return build(caption, (String)null, listener);
	}
	
	public static Button build(final String caption, final VaadinIcons icon, final ClickListener listener) {
		Button button = new Button(caption);
		button.setIcon(icon);
		button.setWidth(100.0f, Unit.PERCENTAGE);
		if (listener != null) {
			button.addClickListener(listener);
		}
        return button;
	}
	
	public static Button build(final String caption, final VaadinIcons icon, final ClickListener listener, final String...styles) {
		Button button = build(caption, icon, listener);
		if (styles != null && styles.length > 0) {
			for(String style : styles) {
				button.addStyleName(style);
			}
		}
        return button;
	}
	
	public static Button build(final String caption, final String styleName, final ClickListener listener) {
		Button button = new Button(caption);
		if (StringUtils.isNotBlank(styleName)) {
			button.setStyleName(styleName);
		}
		button.setWidth(100.0f, Unit.PERCENTAGE);
		if (listener != null) {
			button.addClickListener(listener);
		}
        return button;
	}
	
	public static Button SAVE(final ClickListener listener, final String...styles) {
		return ButtonBuilder.build(I8n.Button.SAVE, VaadinIcons.CHECK_CIRCLE, listener, styles);
	}
	
	public static Button CANCEL(final ClickListener listener, final String...styles) {
		return ButtonBuilder.build(I8n.Button.CANCEL, null, listener, styles);	
	}
	
	public static Button RESET(final ClickListener listener, final String...styles) {
		return ButtonBuilder.build(I8n.Button.RESET, VaadinIcons.REFRESH, listener, styles);
	}
	
	public static Button NEW(final ClickListener listener, final String...styles) {
		return ButtonBuilder.build(I8n.Button.NEW, VaadinIcons.ASTERISK, listener, styles);
	}
	
	public static Button DELETE(final ClickListener listener, final String...styles) {
		return ButtonBuilder.build(I8n.Button.DELETE, VaadinIcons.TRASH, listener, styles);
	}
	
	public static Button CHANGE(final ClickListener listener, final String...styles) {
		return ButtonBuilder.build(I8n.Button.CHANGE, VaadinIcons.UPLOAD, listener, styles);
	}
	
	public static Button RESET_PASSWORD(final ClickListener listener, final String...styles) {
		return ButtonBuilder.build(I8n.Button.RESET_PASSWORD, VaadinIcons.ARROW_BACKWARD, listener, styles);
	}
	
	public static Button CLEAR_ALL(final ClickListener listener, final String...styles) {
		Button button = ButtonBuilder.build(I8n.Button.CLEAR_ALL, null, listener, styles);	
		button.setWidthUndefined();
		return button;
	}
	
	public static Button DASHBOARD_EDIT(final String id, final ClickListener listener) {
		Button button =  ButtonBuilder.build(I8n.Button.EDIT, VaadinIcons.EDIT, listener, Style.Button.ICON_EDIT, ValoTheme.BUTTON_ICON_ONLY);
		button.setId(id);
		button.setDescription("Edit Dashboard");
		return button;
	}
}

