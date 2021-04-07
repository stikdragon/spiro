package uk.co.stikman.spiro.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Spiro implements EntryPoint {
	public static final float	PI			= 3.14159f;
	public static final float	PI2			= PI * 2;

	private static final int	LEFTSIZE	= 200;
	private static final int	PADDING		= 4;
	private static final float	STEPSIZE	= PI2 / 100000.0f;

	private TextArea			txtInstructions;
	private Canvas				cnv;

	public void onModuleLoad() {
		RootLayoutPanel root = RootLayoutPanel.get();

		cnv = Canvas.createIfSupported();
		root.add(cnv);
		root.setWidgetLeftRight(cnv, LEFTSIZE + PADDING, Unit.PX, 0, Unit.PX);
		root.setWidgetTopBottom(cnv, 0, Unit.PX, 0, Unit.PX);

		LayoutPanel lp = new LayoutPanel();
		root.add(lp);
		root.setWidgetLeftWidth(lp, 0, Unit.PX, LEFTSIZE, Unit.PX);
		root.setWidgetTopHeight(lp, 0, Unit.PX, 300, Unit.PX);

		HTML html = new HTML();
		html.setHTML("<b>Spiro</b><br/><br/>Very rudimentary Spirograph thing<br/><br/>Have a look at the samples for idea of how to use it");
		lp.add(html);
		lp.setWidgetLeftRight(html, 0, Unit.PX, 0, Unit.PX);
		lp.setWidgetTopBottom(html, 0, Unit.PX, 40, Unit.PX);

		HorizontalPanel hp = new HorizontalPanel();
		lp.add(hp);
		lp.setWidgetLeftRight(hp, 0, Unit.PX, 0, Unit.PX);
		lp.setWidgetBottomHeight(hp, 0, Unit.PX, 40, Unit.PX);

		Button btnRun = new Button("Run");
		hp.add(btnRun);
		btnRun.addClickHandler(this::run);

		Button btnSample = new Button("Samples...");
		hp.add(btnSample);
		btnSample.addClickHandler(this::samples);

		txtInstructions = new TextArea();
		root.add(txtInstructions);
		root.setWidgetLeftWidth(txtInstructions, 0, Unit.PX, LEFTSIZE, Unit.PX);
		root.setWidgetTopBottom(txtInstructions, 300 + PADDING, Unit.PX, 0, Unit.PX);
		txtInstructions.setText(Samples.get().get("sample1"));

		Window.addResizeHandler(this::resize);
		Scheduler.get().scheduleDeferred(() -> {
			resize(null);
			run(null);
		});
	}

	private void resize(ResizeEvent ev) {
		cnv.setCoordinateSpaceWidth(cnv.getOffsetWidth());
		cnv.setCoordinateSpaceHeight(cnv.getOffsetHeight());
	}

	private void samples(ClickEvent ev) {
		LayoutPanel lp = new LayoutPanel();
		ScrollPanel sp = new ScrollPanel();
		lp.add(sp);
		lp.setWidgetLeftRight(sp, 0, Unit.PX, 0, Unit.PX);
		lp.setWidgetTopBottom(sp, 0, Unit.PX, 0, Unit.PX);

		VerticalPanel vp = new VerticalPanel();
		sp.setWidget(vp);

		DialogBox dlg = new DialogBox(true, true);

		for (String k : Samples.get().keys()) {
			Label l = new Label(k);
			l.addClickHandler(event -> {
				dlg.hide();
				loadSample(k);
			});
			l.addStyleName("sample");
			vp.add(l);
		}

		dlg.setText("Samples");
		dlg.setGlassEnabled(true);
		dlg.setWidget(lp);
		lp.setSize("500px", "300px");
		dlg.show();
		dlg.center();

	}

	private void loadSample(String key) {
		txtInstructions.setText(Samples.get().get(key));
		run(null);
	}

	private void run(ClickEvent ev) {
		Vector2 v = new Vector2();

		SpiroSet set = new SpiroSet();
		try {
			set.parse(txtInstructions.getText());
		} catch (Exception e) {
			Window.alert("ERROR: " + e.getMessage());
			throw e;
		}

		Context2d ctx = cnv.getContext2d();
		float w = cnv.getOffsetWidth();
		float h = cnv.getOffsetHeight();
		ctx.clearRect(0, 0, w, h);
		for (Figure fig : set.getFigures()) {
			ctx.setStrokeStyle(fig.getColour());
			ctx.setLineWidth(fig.getWidth());
			v.set(w / 2, h / 2);
			fig.eval(0, v);
			ctx.beginPath();
			ctx.moveTo(v.x, v.y);
			for (float f = 0.0f; f < PI2; f += STEPSIZE) {
				v.set(w / 2, h / 2);
				fig.eval(f, v);
				ctx.lineTo(v.x, v.y);
			}
			ctx.stroke();
		}

	}
}
