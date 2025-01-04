package jqsx.scripts;

import KanapkaEngine.Components.Mathf;
import KanapkaEngine.Components.RenderLayer;
import KanapkaEngine.Components.RenderStage;
import KanapkaEngine.Game.Plugin;
import KanapkaEngine.RenderLayers.World;
import jqsx.Game;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import static jqsx.scripts.DamageIndicator.indicators;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class DI_RenderLayer extends Plugin implements RenderLayer {
    @Override
    public void Render(Graphics2D g) {
        indicators.foreach(damageIndicator -> {
            AffineTransform at = Mathf.getTransform(damageIndicator.position, new Vector2D(16, 16));

            g.setFont(Game.global_font.deriveFont(10f));
            g.setColor(new Color(0xff0000));

            g.drawString(damageIndicator.text, (int) at.getTranslateX(), (int) at.getTranslateY());
        });
    }

    @Override
    public void Update() {
        indicators.removeIf(DamageIndicator::isDead);
    }

    @Override
    public RenderStage getStage() {
        return RenderStage.PARTICLES;
    }
}
