package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;

import java.awt.*;

public abstract class NES_Carn_color_base_hullmod extends BaseHullMod {

    public static final String MIDLINE = "NES_Midline";
    public static final String REEF = "NES_Reef";
    public static final String TOXIC = "NES_Toxic";
    public static final String TURQUOISE = "NES_Turquoise";
    public static final String PERSEAN = "NES_Persean";
    public static final String AGENT = "NES_Agent";


    private static final String ALT_SPRITES = "nes_alt_sprites";

    protected abstract String getHullModId();

    protected abstract String getAltSpriteSuffix();

    protected abstract void updateDecoWeapons(ShipAPI ship);

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        if (getAltSpriteSuffix() != null) {
            String spriteId = ship.getHullSpec().getBaseHullId() + getAltSpriteSuffix();
            SpriteAPI sprite;
            try {
                sprite = Global.getSettings().getSprite(ALT_SPRITES, spriteId, false);
            } catch (RuntimeException ex) {
                sprite = null;
            }

            if (sprite != null) {
                float x = ship.getSpriteAPI().getCenterX();
                float y = ship.getSpriteAPI().getCenterY();
                float alpha = ship.getSpriteAPI().getAlphaMult();
                float angle = ship.getSpriteAPI().getAngle();
                Color color = ship.getSpriteAPI().getColor();

                ship.setSprite(ALT_SPRITES, spriteId);
                //ShaderLib.overrideShipTexture(ship, spriteId);

                ship.getSpriteAPI().setCenter(x, y);
                ship.getSpriteAPI().setAlphaMult(alpha);
                ship.getSpriteAPI().setAngle(angle);
                ship.getSpriteAPI().setColor(color);
            }
        }

        updateDecoWeapons(ship);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        updateDecoWeapons(ship);
    }
}
