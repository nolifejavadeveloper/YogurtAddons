package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.Set;

public class RenderUtil {
    private RenderUtil() {
    }

    public static void drawBoxOutline(AxisAlignedBB bb, int r, int g, int b, int a, boolean ignoreDepth) {
        double camX = Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double camY = Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double camZ = Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        AxisAlignedBB bbOffset = bb.offset(-camX, -camY, -camZ);

        GlStateManager.pushMatrix();
        GL11.glLineWidth(2.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableCull();

        if (ignoreDepth) {
            GlStateManager.depthMask(false);
        }

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        wr.pos(bbOffset.minX, bbOffset.minY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.minY, bbOffset.minZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.maxX, bbOffset.minY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.minY, bbOffset.maxZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.minX, bbOffset.minY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.minY, bbOffset.maxZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.minX, bbOffset.minY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.minX, bbOffset.minY, bbOffset.minZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.minX, bbOffset.maxY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.maxY, bbOffset.minZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.maxX, bbOffset.maxY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.maxY, bbOffset.maxZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.maxX, bbOffset.maxY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.minX, bbOffset.maxY, bbOffset.maxZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.minX, bbOffset.maxY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.minX, bbOffset.maxY, bbOffset.minZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.minX, bbOffset.minY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.minX, bbOffset.maxY, bbOffset.minZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.maxX, bbOffset.minY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.maxY, bbOffset.minZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.maxX, bbOffset.minY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.maxY, bbOffset.maxZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.minX, bbOffset.minY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.minX, bbOffset.maxY, bbOffset.maxZ).color(r, g, b, a).endVertex();

        tessellator.draw();

        if (ignoreDepth) {
            GlStateManager.depthMask(true);
        }

        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBoxFaces(AxisAlignedBB bb, int r, int g, int b, int a, boolean ignoreDepth) {
        double camX = Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double camY = Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double camZ = Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        AxisAlignedBB bbOffset = bb.offset(-camX, -camY, -camZ);

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableCull();

        if (ignoreDepth) {
            GlStateManager.depthMask(false);
        }

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        wr.pos(bbOffset.minX, bbOffset.minY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.minY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.minY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.minX, bbOffset.minY, bbOffset.maxZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.minX, bbOffset.maxY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.minX, bbOffset.maxY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.maxY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.maxY, bbOffset.minZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.minX, bbOffset.minY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.minX, bbOffset.maxY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.maxY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.minY, bbOffset.minZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.minX, bbOffset.minY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.minY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.maxY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.minX, bbOffset.maxY, bbOffset.maxZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.minX, bbOffset.minY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.minX, bbOffset.minY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.minX, bbOffset.maxY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.minX, bbOffset.maxY, bbOffset.minZ).color(r, g, b, a).endVertex();

        wr.pos(bbOffset.maxX, bbOffset.minY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.maxY, bbOffset.minZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.maxY, bbOffset.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bbOffset.maxX, bbOffset.minY, bbOffset.maxZ).color(r, g, b, a).endVertex();

        tessellator.draw();

        if (ignoreDepth) {
            GlStateManager.depthMask(true);
        }

        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
