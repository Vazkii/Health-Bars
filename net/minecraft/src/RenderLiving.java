package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderLiving extends Render
{
    protected ModelBase mainModel;

    /** The model to be used during the render passes. */
    protected ModelBase renderPassModel;

    public RenderLiving(ModelBase par1ModelBase, float par2)
    {
        this.mainModel = par1ModelBase;
        this.shadowSize = par2;
    }

    /**
     * Sets the model to be used in the current render pass (the first render pass is done after the primary model is
     * rendered) Args: model
     */
    public void setRenderPassModel(ModelBase par1ModelBase)
    {
        this.renderPassModel = par1ModelBase;
    }

    private float func_48418_a(float par1, float par2, float par3)
    {
        float var4;

        for (var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F)
        {
            ;
        }

        while (var4 >= 180.0F)
        {
            var4 -= 360.0F;
        }

        return par1 + par3 * var4;
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.mainModel.onGround = this.renderSwingProgress(par1EntityLiving, par9);

        if (this.renderPassModel != null)
        {
            this.renderPassModel.onGround = this.mainModel.onGround;
        }

        this.mainModel.isRiding = par1EntityLiving.isRiding();

        if (this.renderPassModel != null)
        {
            this.renderPassModel.isRiding = this.mainModel.isRiding;
        }

        this.mainModel.isChild = par1EntityLiving.isChild();

        if (this.renderPassModel != null)
        {
            this.renderPassModel.isChild = this.mainModel.isChild;
        }

        try
        {
            float var10 = this.func_48418_a(par1EntityLiving.prevRenderYawOffset, par1EntityLiving.renderYawOffset, par9);
            float var11 = this.func_48418_a(par1EntityLiving.prevRotationYaw, par1EntityLiving.rotationYaw, par9);
            float var12 = par1EntityLiving.prevRotationPitch + (par1EntityLiving.rotationPitch - par1EntityLiving.prevRotationPitch) * par9;
            this.renderLivingAt(par1EntityLiving, par2, par4, par6);
            float var13 = this.handleRotationFloat(par1EntityLiving, par9);
            this.rotateCorpse(par1EntityLiving, var13, var10, par9);
            float var14 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(par1EntityLiving, par9);
            GL11.glTranslatef(0.0F, -24.0F * var14 - 0.0078125F, 0.0F);
            float var15 = par1EntityLiving.field_705_Q + (par1EntityLiving.field_704_R - par1EntityLiving.field_705_Q) * par9;
            float var16 = par1EntityLiving.field_703_S - par1EntityLiving.field_704_R * (1.0F - par9);

            if (par1EntityLiving.isChild())
            {
                var16 *= 3.0F;
            }

            if (var15 > 1.0F)
            {
                var15 = 1.0F;
            }

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            this.mainModel.setLivingAnimations(par1EntityLiving, var16, var15, par9);
            this.renderModel(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
            float var19;
            int var18;
            float var20;
            float var22;

            for (int var17 = 0; var17 < 4; ++var17)
            {
                var18 = this.shouldRenderPass(par1EntityLiving, var17, par9);

                if (var18 > 0)
                {
                    this.renderPassModel.setLivingAnimations(par1EntityLiving, var16, var15, par9);
                    this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

                    if (var18 == 15)
                    {
                        var19 = (float)par1EntityLiving.ticksExisted + par9;
                        this.loadTexture("%blur%/misc/glint.png");
                        GL11.glEnable(GL11.GL_BLEND);
                        var20 = 0.5F;
                        GL11.glColor4f(var20, var20, var20, 1.0F);
                        GL11.glDepthFunc(GL11.GL_EQUAL);
                        GL11.glDepthMask(false);

                        for (int var21 = 0; var21 < 2; ++var21)
                        {
                            GL11.glDisable(GL11.GL_LIGHTING);
                            var22 = 0.76F;
                            GL11.glColor4f(0.5F * var22, 0.25F * var22, 0.8F * var22, 1.0F);
                            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                            GL11.glMatrixMode(GL11.GL_TEXTURE);
                            GL11.glLoadIdentity();
                            float var23 = var19 * (0.001F + (float)var21 * 0.003F) * 20.0F;
                            float var24 = 0.33333334F;
                            GL11.glScalef(var24, var24, var24);
                            GL11.glRotatef(30.0F - (float)var21 * 60.0F, 0.0F, 0.0F, 1.0F);
                            GL11.glTranslatef(0.0F, var23, 0.0F);
                            GL11.glMatrixMode(GL11.GL_MODELVIEW);
                            this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
                        }

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glDepthMask(true);
                        GL11.glLoadIdentity();
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        GL11.glEnable(GL11.GL_LIGHTING);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glDepthFunc(GL11.GL_LEQUAL);
                    }

                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                }
            }

            this.renderEquippedItems(par1EntityLiving, par9);
            float var26 = par1EntityLiving.getBrightness(par9);
            var18 = this.getColorMultiplier(par1EntityLiving, var26, par9);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

            if ((var18 >> 24 & 255) > 0 || par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);

                if (par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0)
                {
                    GL11.glColor4f(var26, 0.0F, 0.0F, 0.4F);
                    this.mainModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

                    for (int var27 = 0; var27 < 4; ++var27)
                    {
                        if (this.inheritRenderPass(par1EntityLiving, var27, par9) >= 0)
                        {
                            GL11.glColor4f(var26, 0.0F, 0.0F, 0.4F);
                            this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
                        }
                    }
                }

                if ((var18 >> 24 & 255) > 0)
                {
                    var19 = (float)(var18 >> 16 & 255) / 255.0F;
                    var20 = (float)(var18 >> 8 & 255) / 255.0F;
                    float var29 = (float)(var18 & 255) / 255.0F;
                    var22 = (float)(var18 >> 24 & 255) / 255.0F;
                    GL11.glColor4f(var19, var20, var29, var22);
                    this.mainModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

                    for (int var28 = 0; var28 < 4; ++var28)
                    {
                        if (this.inheritRenderPass(par1EntityLiving, var28, par9) >= 0)
                        {
                            GL11.glColor4f(var19, var20, var29, var22);
                            this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
                        }
                    }
                }

                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        catch (Exception var25)
        {
            var25.printStackTrace();
        }

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        
    	if(Minecraft.isGuiEnabled() && mod_HealthBars.barsEnabled && !par1EntityLiving.worldObj.isRemote)
    	renderHealthBar(par1EntityLiving, par2, (par4+mod_HealthBars.barHeight)  + par1EntityLiving.getEyeHeight(), par6, mod_HealthBars.renderDistance);
        this.passSpecialRender(par1EntityLiving, par2, par4, par6);
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityLiving par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        this.loadDownloadableImageTexture(par1EntityLiving.skinUrl, par1EntityLiving.getTexture());
        this.mainModel.render(par1EntityLiving, par2, par3, par4, par5, par6, par7);
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    protected void renderLivingAt(EntityLiving par1EntityLiving, double par2, double par4, double par6)
    {
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
    }

    protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4)
    {
        GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);

        if (par1EntityLiving.deathTime > 0)
        {
            float var5 = ((float)par1EntityLiving.deathTime + par4 - 1.0F) / 20.0F * 1.6F;
            var5 = MathHelper.sqrt_float(var5);

            if (var5 > 1.0F)
            {
                var5 = 1.0F;
            }

            GL11.glRotatef(var5 * this.getDeathMaxRotation(par1EntityLiving), 0.0F, 0.0F, 1.0F);
        }
    }

    protected float renderSwingProgress(EntityLiving par1EntityLiving, float par2)
    {
        return par1EntityLiving.getSwingProgress(par2);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLiving par1EntityLiving, float par2)
    {
        return (float)par1EntityLiving.ticksExisted + par2;
    }

    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {}

    protected int inheritRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return this.shouldRenderPass(par1EntityLiving, par2, par3);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return -1;
    }

    protected float getDeathMaxRotation(EntityLiving par1EntityLiving)
    {
        return 90.0F;
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityLiving par1EntityLiving, float par2, float par3)
    {
        return 0;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {}

    /**
     * Passes the specialRender and renders it
     */
    protected void passSpecialRender(EntityLiving par1EntityLiving, double par2, double par4, double par6)
    {
        if (Minecraft.isDebugInfoEnabled())
        {
            ;
        }
    }

    /**
     * Draws the debug or playername text above a living
     */
    protected void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9)
    {
        float var10 = par1EntityLiving.getDistanceToEntity(this.renderManager.livingPlayer);

        if (var10 <= (float)par9)
        {
            FontRenderer var11 = this.getFontRendererFromRenderManager();
            float var12 = 1.6F;
            float var13 = 0.016666668F * var12;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + 2.3F, (float)par7);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-var13, -var13, var13);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator var14 = Tessellator.instance;
            byte var15 = 0;

            if (par2Str.equals("deadmau5"))
            {
                var15 = -10;
            }

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            var14.startDrawingQuads();
            int var16 = var11.getStringWidth(par2Str) / 2;
            var14.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            var14.addVertex((double)(-var16 - 1), (double)(-1 + var15), 0.0D);
            var14.addVertex((double)(-var16 - 1), (double)(8 + var15), 0.0D);
            var14.addVertex((double)(var16 + 1), (double)(8 + var15), 0.0D);
            var14.addVertex((double)(var16 + 1), (double)(-1 + var15), 0.0D);
            var14.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, var15, 553648127);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, var15, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
    }
    
    public void renderHealthBar(EntityLiving entityliving, double d, double d1, double d2, int i)
    {
        float f = entityliving.getDistanceToEntity(renderManager.livingPlayer);
        if(f > (float)i || !entityliving.canEntityBeSeen(renderManager.livingPlayer) || entityliving.getHealth() <= 0 || entityliving instanceof EntityPlayer || mod_HealthBars.isEntityBlackListed(entityliving))
        return;
        
        if(mod_HealthBars.healthNumbers)
        renderLivingLabel(entityliving, new StringBuilder().append((double)entityliving.getHealth()/2).append("/").append((double)entityliving.getMaxHealth()/2).append(" (").append(entityliving.getHealth() * 100 / entityliving.getMaxHealth()).append("%)").toString(), d, d1+0.25, d2, i);
        
        if(entityliving.getHealth() > mod_HealthBars.maxHealthToRender || entityliving.getHealth() < mod_HealthBars.minHealthToRender)
        return;
        
        float f1 = 1.6F;
        float f2 = 0.01666667F * f1;                                      
        GL11.glPushMatrix();                                              
        GL11.glTranslatef((float)d + 0.0F, (float)d1 + 2.3F, (float)d2);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);                                
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);   
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);     
        GL11.glScalef(-f2, -f2, f2);                                       
        GL11.glDisable(2896 /*GL_LIGHTING*/);                              
        GL11.glDepthMask(false);                                        
        GL11.glDisable(2929 /*GL_DEPTH_TEST*/);                          
        GL11.glEnable(3042 /*GL_BLEND*/);                             
        GL11.glBlendFunc(770, 771);                                       
        Tessellator tessellator = Tessellator.instance;                   
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/); 
        tessellator.startDrawingQuads();
        int j = entityliving.getMaxHealth();
        tessellator.setColorRGBA_F(mod_HealthBars.barOutlineRGBA[0], mod_HealthBars.barOutlineRGBA[1], mod_HealthBars.barOutlineRGBA[2], mod_HealthBars.barOutlineRGBA[3] - (f / 20));
        tessellator.addVertex(-j - 3 , -1, 0.0D); // O		 O
        										  // X 		 O
        
        tessellator.addVertex(-j - 1, 3, 0.0D);   // X		 O
        										  // O 		 O
        
        tessellator.addVertex(j + 3, 3, 0.0D);    // O		 X
        										  // O 		 O
        
        tessellator.addVertex(j + 1, -1, 0.0D);   // O		 O
        										  // O 		 X
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(mod_HealthBars.barBackgroundRGBA[0], mod_HealthBars.barBackgroundRGBA[1], mod_HealthBars.barBackgroundRGBA[2], mod_HealthBars.barBackgroundRGBA[3] - (f / 20));
        tessellator.addVertex(-j - 1, 0, 0.0D);
        tessellator.addVertex(-j, 2, 0.0D);
        tessellator.addVertex(j + 1, 2, 0.0D);
        tessellator.addVertex(j, 0, 0.0D);
        tessellator.draw();
        tessellator.startDrawingQuads();
        int p = entityliving.getHealth();
        if(entityliving.isPotionActive(Potion.poison))
        tessellator.setColorRGBA_F(mod_HealthBars.barPoisonedRGBA[0], mod_HealthBars.barPoisonedRGBA[1], mod_HealthBars.barPoisonedRGBA[2], mod_HealthBars.barPoisonedRGBA[3] - (f / 20));
        else if(entityliving.getAttackTarget() == renderManager.livingPlayer)
        tessellator.setColorRGBA_F(mod_HealthBars.barAggroRGBA[0], mod_HealthBars.barAggroRGBA[1], mod_HealthBars.barAggroRGBA[2], mod_HealthBars.barAggroRGBA[3] - (f / 20));
        else tessellator.setColorRGBA_F(mod_HealthBars.barNonAggroRGBA[0], mod_HealthBars.barNonAggroRGBA[1], mod_HealthBars.barNonAggroRGBA[2], mod_HealthBars.barNonAggroRGBA[3] - (f / 20));
        if(!mod_HealthBars.alignLeft){
            tessellator.addVertex(-p - 1, 0, 0.0D);
            tessellator.addVertex(-p, 2, 0.0D);
            tessellator.addVertex(p + 1, 2, 0.0D);
            tessellator.addVertex(p, 0, 0.0D);
        }else{
            tessellator.addVertex(-j - 1, 0, 0.0D);
            tessellator.addVertex(-j , 2, 0.0D);
            tessellator.addVertex(p*2-j + 1, 2, 0.0D);
            tessellator.addVertex(p*2-j, 0, 0.0D);
        }
        tessellator.draw();
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
        GL11.glDepthMask(true);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        }
    

    }
