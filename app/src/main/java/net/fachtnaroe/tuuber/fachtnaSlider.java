package net.fachtnaroe.tuuber;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.SdkLevel;

@DesignerComponent(
        version = 2,
        description = "A Slider is a progress bar that adds a draggable thumb. You can touch the thumb and drag left or right to set the slider thumb position. As the Slider thumb is dragged, it will trigger the PositionChanged event, reporting the position of the Slider thumb. The reported position of the Slider thumb can be used to dynamically update another component attribute, such as the font size of a TextBox or the radius of a Ball.",
        category = ComponentCategory.USERINTERFACE
)
@SimpleObject
public class fachtnaSlider extends AndroidViewComponent implements OnSeekBarChangeListener {
    private static final String LOG_TAG = "Slider";
    private static final boolean DEBUG = false;
    private final SeekBar seekbar;
    private float minValue;
    private float maxValue;
    private float thumbPosition;
    private boolean thumbEnabled;
    private LayerDrawable fullBar;
    // FR CHANGED:
    // Was: private ClipDrawable beforeThumb;
    private Drawable beforeThumb;
    private int rightColor;
    private int leftColor;
    private static final int initialRightColor = -7829368;
    private static final String initialRightColorString = "&HFF888888";
    private static final int initialLeftColor = -14336;
    private static final String initialLeftColorString = "&HFFFFC800";
    public final boolean referenceGetThumb = SdkLevel.getLevel() >= 16;

    public fachtnaSlider(ComponentContainer container) {
        super(container);
        this.seekbar = new SeekBar(container.$context());
        this.fullBar = (LayerDrawable)this.seekbar.getProgressDrawable();
        // FR CHANGED:
        // Was:  this.beforeThumb = (ClipDrawable)this.fullBar.findDrawableByLayerId(16908301);
        this.beforeThumb = this.fullBar.findDrawableByLayerId(16908301);
        this.leftColor = -14336;
        this.rightColor = -7829368;
        this.setSliderColors();
        container.$add(this);
        this.minValue = 10.0F;
        this.maxValue = 50.0F;
        this.thumbPosition = 30.0F;
        this.thumbEnabled = true;
        this.seekbar.setOnSeekBarChangeListener(this);
        this.seekbar.setMax(100);
        this.setSeekbarPosition();
    }

    private void setSliderColors() {
        this.fullBar.setColorFilter(this.rightColor, Mode.SRC);
        this.beforeThumb.setColorFilter(this.leftColor, Mode.SRC);
    }

    private void setSeekbarPosition() {
        float seekbarPosition = (this.thumbPosition - this.minValue) / (this.maxValue - this.minValue) * 100.0F;
        this.seekbar.setProgress((int)seekbarPosition);
    }

    @DesignerProperty(
            editorType = "boolean",
            defaultValue = "True"
    )
    @SimpleProperty(
            description = "Sets whether or not to display the slider thumb.",
            userVisible = true
    )
    public void ThumbEnabled(boolean enabled) {
        this.thumbEnabled = enabled;
        int alpha = this.thumbEnabled ? 255 : 0;
        if (this.referenceGetThumb) {
            // FR CHANGED:
            (new net.fachtnaroe.tuuber.fachtnaSlider.SeekBarHelper()).getThumb(alpha);
        }

        this.seekbar.setEnabled(this.thumbEnabled);
    }

    @SimpleProperty(
            category = PropertyCategory.APPEARANCE,
            description = "Returns whether or not the slider thumb is being be shown",
            userVisible = true
    )
    public boolean ThumbEnabled() {
        return this.thumbEnabled;
    }

    @DesignerProperty(
            editorType = "float",
            defaultValue = "30.0"
    )
    @SimpleProperty(
            description = "Sets the position of the slider thumb. If this value is greater than MaxValue, then it will be set to same value as MaxValue. If this value is less than MinValue, then it will be set to same value as MinValue.",
            userVisible = true
    )
    public void ThumbPosition(float position) {
        this.thumbPosition = Math.max(Math.min(position, this.maxValue), this.minValue);
        this.setSeekbarPosition();
        this.PositionChanged(this.thumbPosition);
    }

    @SimpleProperty(
            category = PropertyCategory.APPEARANCE,
            description = "Returns the position of slider thumb",
            userVisible = true
    )
    public float ThumbPosition() {
        return this.thumbPosition;
    }

    @DesignerProperty(
            editorType = "float",
            defaultValue = "10.0"
    )
    @SimpleProperty(
            description = "Sets the minimum value of slider.  Changing the minimum value also resets Thumbposition to be halfway between the (new) minimum and the maximum. If the new minimum is greater than the current maximum, then minimum and maximum will both be set to this value.  Setting MinValue resets the thumb position to halfway between MinValue and MaxValue and signals the PositionChanged event.",
            userVisible = true
    )
    public void MinValue(float value) {
        this.minValue = value;
        this.maxValue = Math.max(value, this.maxValue);
        this.ThumbPosition((this.minValue + this.maxValue) / 2.0F);
    }

    @SimpleProperty(
            category = PropertyCategory.APPEARANCE,
            description = "Returns the value of slider min value.",
            userVisible = true
    )
    public float MinValue() {
        return this.minValue;
    }

    @DesignerProperty(
            editorType = "float",
            defaultValue = "50.0"
    )
    @SimpleProperty(
            description = "Sets the maximum value of slider.  Changing the maximum value also resets Thumbposition to be halfway between the minimum and the (new) maximum. If the new maximum is less than the current minimum, then minimum and maximum will both be set to this value.  Setting MaxValue resets the thumb position to halfway between MinValue and MaxValue and signals the PositionChanged event.",
            userVisible = true
    )
    public void MaxValue(float value) {
        this.maxValue = value;
        this.minValue = Math.min(value, this.minValue);
        this.ThumbPosition((this.minValue + this.maxValue) / 2.0F);
    }

    @SimpleProperty(
            category = PropertyCategory.APPEARANCE,
            description = "Returns the slider max value.",
            userVisible = true
    )
    public float MaxValue() {
        return this.maxValue;
    }

    @SimpleProperty(
            description = "The color of slider to the left of the thumb.",
            category = PropertyCategory.APPEARANCE
    )
    public int ColorLeft() {
        return this.leftColor;
    }

    @DesignerProperty(
            editorType = "color",
            defaultValue = "&HFFFFC800"
    )
    @SimpleProperty
    public void ColorLeft(int argb) {
        this.leftColor = argb;
        this.setSliderColors();
    }

    @SimpleProperty(
            description = "The color of slider to the left of the thumb.",
            category = PropertyCategory.APPEARANCE
    )
    public int ColorRight() {
        return this.rightColor;
    }

    @DesignerProperty(
            editorType = "color",
            defaultValue = "&HFF888888"
    )
    @SimpleProperty
    public void ColorRight(int argb) {
        this.rightColor = argb;
        this.setSliderColors();
    }

    public View getView() {
        return this.seekbar;
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.thumbPosition = (this.maxValue - this.minValue) * (float)progress / 100.0F + this.minValue;
        this.PositionChanged(this.thumbPosition);
    }

    @SimpleEvent
    public void PositionChanged(float thumbPosition) {
        EventDispatcher.dispatchEvent(this, "PositionChanged", new Object[]{thumbPosition});
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public int Height() {
        return this.getView().getHeight();
    }

    public void Height(int height) {
        this.container.setChildHeight(this, height);
    }

    private class SeekBarHelper {
        private SeekBarHelper() {
        }

        public void getThumb(int alpha) {
            // FR CHANGED:
            net.fachtnaroe.tuuber.fachtnaSlider.this.seekbar.getThumb().mutate().setAlpha(alpha);
        }
    }
}
