from PPlay.sprite import Sprite
from PPlay.window import Window
from PPlay.gameimage import GameImage
from PPlay.sound import Sound
import PPlay.sound
import random
import math

Tam_x, Tam_y = 1440, 900
janela = Window(Tam_x, Tam_y)
janela.set_title("Versão Final")

botao_jogar = Sprite("claq_a.xcf")
botao_opcao = Sprite("claq_a.xcf")
botao_sair = Sprite("claq_a.xcf")
botao_single = Sprite("claq_a.xcf")
botao_mult = Sprite("claq_a.xcf")
botao_volta = Sprite("claq_a.xcf")

HUB_W = Sprite("Hub_W.xcf")
HUB_W.set_position(20, 20)
HUB_F = Sprite("HUB_F.xcf")
HUB_F.set_position(janela.width - HUB_F.width - 20, 20)

botao_jogar.set_position(janela.width/2 - botao_opcao.width/2, 350)
botao_opcao.set_position(janela.width/2 - botao_opcao.width/2, 500) 
botao_sair.set_position(janela.width/2 - botao_sair.width/2, 650)
botao_single.set_position(janela.width/2 - botao_sair.width/2, 350)
botao_mult.set_position(janela.width/2 - botao_sair.width/2 + 400, 650)
botao_volta.set_position(janela.width/2 - botao_sair.width/2 - 400, 650)

# Variáveis de Controle de Input
mouse_livre = True
mouse = janela.get_mouse()
teclado = janela.get_keyboard()

# Fundos
menu_0 = GameImage("menu_0.jpg")
Fundo = GameImage("fundooooo.png")
Fundo2 = GameImage("fundooooo2.png")
Fundo3 = GameImage("fundooooo3.png")
comeco = GameImage("MISSÃO POSSÍVEL.png")
menu_pausa = Sprite("pausa.xcf") 
menu_pausa.set_position(janela.width/2 - menu_pausa.width/2, janela.height/2 - menu_pausa.height/2)

vel_x, vel_y, vel_tiro = 300, 300, 800
branco = (255, 255, 255)
amarelo = (255, 255, 0)
vermelho = (255, 0, 0)
preto = (0, 0, 0)

flavia = Sprite("fla.xcf")
flavia.set_position(900, 400)

PRESIDENTE = Sprite("presidente.png")
PRESIDENTE.set_position(janela.width/2-PRESIDENTE.width, janela.height/2-PRESIDENTE.height)

cheia = Sprite("vida_cheia.png")
cheia.set_position(janela.width/2-cheia.width/2-cheia.width-40, janela.height*1/9)
cheia2 = Sprite("vida_cheia.png")
cheia2.set_position(janela.width/2-cheia.width/2, janela.height*1/9)
cheia3 = Sprite("vida_cheia.png")
cheia3.set_position(janela.width/2-cheia.width/2+cheia.width+40, janela.height*1/9)
vazia = Sprite("vida_vazia.png")
vazia.set_position(janela.width/2-cheia.width/2-cheia.width-40, janela.height*1/9)
vazia2 = Sprite("vida_vazia.png")
vazia2.set_position(janela.width/2-cheia.width/2, janela.height*1/9)
vazia3 = Sprite("vida_vazia.png")
vazia3.set_position(janela.width/2-cheia.width/2+cheia.width+40, janela.height*1/9)

click_menu = Sound("som/click-menu.ogg")

sons = {
    "dano1": Sound("som/dano1.ogg"),
    "dano2": Sound("som/dano2.ogg"),
    "tiro": Sound("som/disparo_FI56f8W8.ogg"),
    "soco": Sound("som/soco_aAhCO1lZ.ogg"),
    "soco_ini": Sound("som/soquito.ogg"),
    "vida": Sound("som/vida.ogg")
}
sons["soco"].set_volume(5)
sons["soco_ini"].set_volume(5)
controles = {
    "tecla_K": Sprite("controles/K_key.png"),
    "tecla_L": Sprite("controles/L_key.png"),
    "setas": Sprite("controles/setas_.png")
}
controles["setas"].set_position(janela.width/2-controles["setas"].width, janela.height/2)
controles["tecla_K"].set_position(janela.width/2-controles["setas"].width-40, janela.height/2-200)
controles["tecla_L"].set_position(janela.width/2-controles["setas"].width-40, janela.height/2-230-100)
wumberto_animacoes = {
    "anda_esquerda": Sprite("wumb_anim/andando_w (esq).xcf", 8),
    "anda_direita": Sprite("wumb_anim/andando_w.xcf", 8),
    "toma_esquerda": Sprite("wumb_anim/dano_w (esq).xcf", 6),
    "toma_direita": Sprite("wumb_anim/dano_w.xcf", 6),
    "parado_esquerda": Sprite("wumb_anim/idle_w (esq).xcf", 8),
    "parado_direita": Sprite("wumb_anim/idle_w.xcf", 8),
    "da_soco_esquerda": Sprite("wumb_anim/soco_novo(esq).xcf", 8),
    "da_soco_direita": Sprite("wumb_anim/soco_novo.xcf", 8),
    "da_tiro_esquerda": Sprite("wumb_anim/tiro_w (esq).xcf", 4),
    "da_tiro_direita": Sprite("wumb_anim/tiro_w.xcf", 4)
}

for k, anim in wumberto_animacoes.items():
    if "tiro" in k: anim.set_total_duration(500)
    else: anim.set_total_duration(1000)
    anim.set_position(900, 600)

animacao_atual = "parado_direita"
wumberto_atual = wumberto_animacoes[animacao_atual]

tipos_inimigos = [
    {
        "nome": "cap1",
        "paths": {"anda_dir": "cap1_anim/andando_cap1.xcf", "anda_esq": "cap1_anim/andando_cap1 (esq).xcf",
                  "atk_dir": "cap1_anim/soco_cap1.xcf", "atk_esq": "cap1_anim/soco_cap1 (esq).xcf",
                  "toma_dir": "cap1_anim/dano_cap1.xcf", "toma_esq": "cap1_anim/dano_cap1 (esq).xcf"},
        "frames": {"anda": 4, "atk": 3, "toma": 6}
    },
    {
        "nome": "cap2",
        "paths": {"anda_dir": "cap2_anim/andando_cap2.xcf", "anda_esq": "cap2_anim/andando_cap2 (1).xcf",
                  "atk_dir": "cap2_anim/soco_cap2.xcf", "atk_esq": "cap2_anim/soco_cap2 (1).xcf",
                  "toma_dir": "cap2_anim/dano_cap2.xcf", "toma_esq": "cap2_anim/dano_cap2 (1).xcf"},
        "frames": {"anda": 4, "atk": 6, "toma": 6}
    },
    { 
        "nome": "cap3",
        "paths": {"anda_dir": "cap3_anim/andando_cap3.xcf", "anda_esq": "cap3_anim/andando_cap3 (1).xcf",
                  "atk_dir": "cap3_anim/soco_cap3.xcf", "atk_esq": "cap3_anim/soco_cap3 (1).xcf",
                  "toma_dir": "cap3_anim/dano_cap3.xcf", "toma_esq": "cap3_anim/dano_cap3 (1).xcf"},
        "frames": {"anda": 4, "atk": 7, "toma": 6}
    },
    { 
        "nome": "boss",
        "paths": {"anda_dir": "boss_anim/andando_boss.xcf", "anda_esq": "boss_anim/andando_boss (1).xcf",
                  "atk_dir": "boss_anim/chute_boss.xcf", "atk_esq": "boss_anim/chute_boss (1).xcf",
                  "toma_dir": "boss_anim/dano_boss.xcf", "toma_esq": "boss_anim/dano_boss (1).xcf",
                  "morre_dir": "boss_anim/morrendo_boss.xcf", "morre_esq": "boss_anim/morrendo_boss (1).xcf"},
        "frames": {"anda": 4, "atk": 7, "toma": 6, "morre": 7}
    }
]

invencivel_flavia, invencivel_wumberto = False, False
timer_invencibilidade_flavia, timer_invencibilidade_wumberto = 0.0, 0.0
duracao_invencibilidade = 2.0
vida_W, vida_F = 3, 3

Soco = Sprite("vazio.png")
spaws = [1317, 29, 1332, 19, 670, 573, 526, 689]
vidas, inis_ativos, personagens, Frangos = [], [], [], []
tirosD, tirosE = [], []

timer, timerW = 0, 0
vida_ini_padrao = 2
total_tela = 5

fla_D, wum_D, rodando = True, True, True
esc_anterior, space_anterior = False, False
multiplayer = False

frames, n_frames, fps = 0.0, 0, 0
tempo_passado, ultimo_chamado = 0.0, 0.0
state, fase = "comeco", 0
prox_fase = 20

Soco_Ativo, dando_tiro_w = False, False
passou_por_op = False
tempo_soco = 0.0
DURACAO_SOCO = 0.15  
#LARGURA_SOCO, ALTURA_SOCO = 1, 1
#Soco.width, Soco.height = LARGURA_SOCO, ALTURA_SOCO
soco_ja_hitou = False

timer_mensagem_fase = 0.0
duracao_mensagem = 1.5
mostrar_mensagem = True
mensagem_fase_texto = "FASE 1"
boss_spawnado = False
tomando = False

frac_dis_max = 0.66
wumberto = wumberto_atual

def reset():
    global state, vida_F, vida_W, invencivel_flavia, invencivel_wumberto
    global personagens, Frangos, prox_fase, boss_spawnado
    global mensagem_fase_texto, mostrar_mensagem, timer_mensagem_fase, fase
    
    state = "menu"
    vida_F = 3
    vida_W = 3
    fase = 0
    invencivel_flavia = False
    invencivel_wumberto = False
    personagens = []
    inis_ativos.clear()
    Frangos.clear()
    janela.set_background_color((0,0,0))
    prox_fase = 20
    mostrar_mensagem = True
    timer_mensagem_fase = 0.0
    mensagem_fase_texto = "FASE 1"
    boss_spawnado = False

def anda_generico(objeto, cima, baixo, esquerda, direita, dt):
    if teclado.key_pressed(cima): objeto.y -= vel_y * dt
    if teclado.key_pressed(baixo): objeto.y += vel_y * dt
    if teclado.key_pressed(esquerda): objeto.x -= vel_x * dt
    if teclado.key_pressed(direita): objeto.x += vel_x * dt     

def n_sai_da_tela(person):
    if person.x <= 0: person.x = 0
    elif person.x + person.width >= janela.width: person.x = janela.width - person.width
    
    if person.y >= janela.height - person.height: person.y = janela.height - person.height
    elif person.y <= janela.height * frac_dis_max - person.height: person.y = janela.height * frac_dis_max - person.height

def da_tiro_D(person):
    tiroD = Sprite("bala.png")
    tiroD.set_position(person.x + person.width*0.6, person.y + 77) 
    tirosD.append(tiroD)
    sons["tiro"].play()

def da_tiro_E(person):
    tiroE = Sprite("bala_reverse.png")
    tiroE.set_position(person.x + person.width*0.2, person.y + 77) 
    tirosE.append(tiroE)
    sons["tiro"].play()

def tomar_dano_flavia():
    global vida_F, invencivel_flavia, timer_invencibilidade_flavia, state
    if invencivel_flavia: return
    vida_F -= 1
    if vida_F <= 0:
        if multiplayer and vida_W > 0:
            if flavia in personagens: personagens.remove(flavia)
        else: state = "game_over"
    else:
        invencivel_flavia = True
        timer_invencibilidade_flavia = 0.0

def tomar_dano_wumberto():
    global vida_W, invencivel_wumberto, tomando
    global timer_invencibilidade_wumberto, state
    if invencivel_wumberto: return
    vida_W -= 1
    sons["dano1"].play()
    tomando = True
    if vida_W <= 0:
        if multiplayer and vida_F > 0:
            if wumberto in personagens: personagens.remove(wumberto)
        else: state = "game_over"
    else:
        invencivel_wumberto = True
        timer_invencibilidade_wumberto = 0.0

def chama_ini():
    posi_ra = random.randint(0, 3)
    x_pos, y_pos = spaws[posi_ra], spaws[posi_ra+4]
    
    tipo = random.choice(tipos_inimigos[:3]) 
    
    sprites_ini = {
        "anda_dir": Sprite(tipo["paths"]["anda_dir"], tipo["frames"]["anda"]),
        "anda_esq": Sprite(tipo["paths"]["anda_esq"], tipo["frames"]["anda"]),
        "atk_dir": Sprite(tipo["paths"]["atk_dir"], tipo["frames"]["atk"]),
        "atk_esq": Sprite(tipo["paths"]["atk_esq"], tipo["frames"]["atk"]),
        "toma_dir": Sprite(tipo["paths"]["toma_dir"], tipo["frames"]["toma"]),
        "toma_esq": Sprite(tipo["paths"]["toma_esq"], tipo["frames"]["toma"])
    }
    for s in sprites_ini.values(): s.set_total_duration(1000)
    
    largura, altura = sprites_ini["anda_dir"].width, sprites_ini["anda_dir"].height
    x_pos = max(0, min(x_pos, janela.width - largura))
    y_pos = max(janela.height * frac_dis_max - altura, min(y_pos, janela.height - altura))
    
    vida_atual = vida_ini_padrao
    if fase == 1: vida_atual += 1
    
    inimigo = {
        "x": x_pos, "y": y_pos, "largura": largura, "altura": altura,
        "vida": vida_atual, "sprites": sprites_ini, "sprite_atual": sprites_ini["anda_dir"],
        "estado": "andando", "timer_estado": 0.0, "direita": True, "eh_boss": False
    }
    inis_ativos.append(inimigo)

def chama_boss():
    global boss_spawnado
    boss_spawnado = True
    
    posi_ra = random.randint(0, 3)
    x_pos, y_pos = spaws[posi_ra], spaws[posi_ra+4]
    
    tipo = tipos_inimigos[3] 
    
    sprites_ini = {
        "anda_dir": Sprite(tipo["paths"]["anda_dir"], tipo["frames"]["anda"]),
        "anda_esq": Sprite(tipo["paths"]["anda_esq"], tipo["frames"]["anda"]),
        "atk_dir": Sprite(tipo["paths"]["atk_dir"], tipo["frames"]["atk"]),
        "atk_esq": Sprite(tipo["paths"]["atk_esq"], tipo["frames"]["atk"]),
        "toma_dir": Sprite(tipo["paths"]["toma_dir"], tipo["frames"]["toma"]),
        "toma_esq": Sprite(tipo["paths"]["toma_esq"], tipo["frames"]["toma"])
    }
    for s in sprites_ini.values(): s.set_total_duration(1000)
        
    largura, altura = sprites_ini["anda_dir"].width, sprites_ini["anda_dir"].height
    x_pos = max(0, min(x_pos, janela.width - largura))
    y_pos = max(janela.height * frac_dis_max - altura, min(y_pos, janela.height - altura))
    
    inimigo = {
        "x": x_pos, "y": y_pos, "largura": largura, "altura": altura,
        "vida": 30, 
        "sprites": sprites_ini, "sprite_atual": sprites_ini["anda_dir"],
        "estado": "andando", "timer_estado": 0.0, "direita": True, 
        "eh_boss": True,
        "timer_tiro": 0.0
    }
    inis_ativos.append(inimigo)

def gerenciar_inimigos(dt):
    global vida_F, vida_W, prox_fase
    removidos = []
    
    vel_base_fase = (150 + (fase * 30)) * 1.15
    vel_boss = 60 * 1.15

    for i, ini in enumerate(inis_ativos):
        vel_movimento = vel_boss if ini.get("eh_boss") else vel_base_fase

        alvo, dist_min = None, float('inf')
        for p in personagens:
            d = math.sqrt((ini["x"] - p.x)**2 + (ini["y"] - p.y)**2)
            if d < dist_min:
                dist_min = d
                alvo = p
        
        if not alvo:
            ini["sprite_atual"].draw()
            continue

        dx, dy = alvo.x - ini["x"], alvo.y - ini["y"]
        ini["direita"] = dx > 0
        
        if ini["estado"] == "tomando_dano":
            ini["timer_estado"] += dt
            if ini["timer_estado"] >= 0.5: ini["estado"] = "andando"
        
        elif ini["estado"] == "atacando":
            ini["timer_estado"] += dt
            sons["soco_ini"].play()
            if ini["timer_estado"] >= 0.8:
                ini["estado"] = "andando"
                if ini["sprite_atual"].collided(alvo):
                    if alvo == flavia: tomar_dano_flavia()
                    elif alvo == wumberto: tomar_dano_wumberto()
        else:
            if abs(dx) < 120 and abs(dy) < 50:
                ini["estado"] = "atacando"
                ini["timer_estado"] = 0.0
                if ini["direita"]: ini["sprites"]["atk_dir"].set_curr_frame(0)
                else: ini["sprites"]["atk_esq"].set_curr_frame(0)
            else:
                if dist_min > 0:
                    ini["x"] += (dx / dist_min) * vel_movimento * dt
                    ini["y"] += (dy / dist_min) * vel_movimento * dt

        nome_sprite = "anda"
        if ini["estado"] == "tomando_dano": nome_sprite = "toma"
        elif ini["estado"] == "atacando": nome_sprite = "atk"
        
        sulfixo = "_dir" if ini["direita"] else "_esq"
        ini["sprite_atual"] = ini["sprites"][nome_sprite + sulfixo]
        
        if ini["x"] <= 0: ini["x"] = 0
        elif ini["x"] + ini["largura"] >= janela.width: ini["x"] = janela.width - ini["largura"]
        
        lim_y = janela.height * frac_dis_max - ini["altura"]
        if ini["y"] >= janela.height - ini["altura"]: ini["y"] = janela.height - ini["altura"]
        elif ini["y"] <= lim_y: ini["y"] = lim_y
        
        ini["sprite_atual"].set_position(ini["x"], ini["y"])
        ini["sprite_atual"].update()
        ini["sprite_atual"].draw()
        
        if ini["vida"] <= 0: removidos.append(i)

    for i in sorted(removidos, reverse=True):
        morto = inis_ativos.pop(i)
        prox_fase -= 1
        
        if morto.get("eh_boss"):
            prox_fase = 0
            
        if random.randint(1, 5) == 1: chama_frango(morto["x"], morto["y"])

def causar_dano_ini(indice_ini, dano):
    ini = inis_ativos[indice_ini]
    ini["vida"] -= dano
    ini["estado"] = "tomando_dano"
    ini["timer_estado"] = 0.0
    sons["dano2"].play()

def chama_frango(x, y):
    frango = Sprite("Icon-Floor_Chicken (1).xcf")
    frango.set_position(x, y+100)
    Frangos.append(frango)

def atualizar_animacao_wumberto():
    global animacao_atual, wumberto_atual, wum_D
    
    nova_animacao = "parado_direita" if wum_D else "parado_esquerda"

    if teclado.key_pressed("LEFT"):
        nova_animacao = "anda_esquerda"
        wum_D = False
    elif teclado.key_pressed("RIGHT"):
        nova_animacao = "anda_direita"
        wum_D = True
    
    if Soco_Ativo: nova_animacao = "da_soco_direita" if wum_D else "da_soco_esquerda"
    elif dando_tiro_w: nova_animacao = "da_tiro_direita" if wum_D else "da_tiro_esquerda"

    if nova_animacao != animacao_atual:
        animacao_atual = nova_animacao
        wumberto_atual = wumberto_animacoes[animacao_atual]
    
    if "parado" not in animacao_atual: wumberto_atual.update()
    wumberto_atual.set_position(wumberto.x, wumberto.y)

# --- GAME LOOP PRINCIPAL ---#######################################################################
while rodando:   
    delta_time = janela.delta_time()
    
    botao_pressionado = mouse.is_button_pressed(1)
    clique_confirmado = False
    

    if botao_pressionado and mouse_livre:
        mouse_livre = False 
        clique_confirmado = True 
        click_menu.play() 
    
    if not botao_pressionado:
        mouse_livre = True

    esc_atual = teclado.key_pressed("ESC")
    
    if invencivel_flavia:
        timer_invencibilidade_flavia += delta_time
        if timer_invencibilidade_flavia >= duracao_invencibilidade: invencivel_flavia = False
        flavia.visivel = (int(timer_invencibilidade_flavia * 10) % 2 == 0)
    else: flavia.visivel = True
    
    if invencivel_wumberto:
        timer_invencibilidade_wumberto += delta_time
        if timer_invencibilidade_wumberto >= duracao_invencibilidade: invencivel_wumberto = False
        vis = (int(timer_invencibilidade_wumberto * 10) % 2 == 0)
        wumberto.visivel, wumberto_atual.visivel = vis, vis
    else: wumberto.visivel, wumberto_atual.visivel = True, True

    if state == "comeco":
        comeco.draw()
        janela.draw_text("APERTE Q PARA COMEÇAR!!!!!", janela.width/2 - 120, janela.height/2, 35, vermelho, "Calibri", True)
        if teclado.key_pressed("Q"): state = "menu"

    elif state == "menu":
        reset()
        menu_0.draw()        
        botao_jogar.draw(); botao_opcao.draw(); botao_sair.draw()
        janela.draw_text("JOGAR", janela.width/2 - botao_opcao.width/2+ 20, 450, 20, branco, "Calibri", True)
        janela.draw_text("OPÇÕES", janela.width/2 - botao_opcao.width/2+ 20, 600, 20, branco, "Calibri", True)
        janela.draw_text("SAIR", janela.width/2 - botao_opcao.width/2+ 20, 750, 20, branco, "Calibri", True)
       
        if mouse.is_over_object(botao_jogar) and clique_confirmado:
            state = "jogando"
        if mouse.is_over_object(botao_opcao) and clique_confirmado:
            state = "opcao"
        if mouse.is_over_object(botao_sair) and clique_confirmado: 
            break
        
    elif state == "opcao":
        menu_0.draw()
        botao_mult.draw()
        botao_volta.draw()
        janela.draw_text("VOLTAR", janela.width/2 - botao_sair.width/2 - 380, 750, 20, branco, "Calibri", True)
        controles["setas"].draw()
        controles["tecla_K"].draw()
        controles["tecla_L"].draw()
        janela.draw_text("ANDA", janela.width/2-controles["setas"].width+400, janela.height/2+50, 30, branco, "Calibri", True)
        janela.draw_text("SOCO", janela.width/2-controles["setas"].width+400, janela.height/2-200+70, 30, branco, "Calibri", True)
        janela.draw_text("TIRO", janela.width/2-controles["setas"].width+400, janela.height/2-230-100+70, 30, branco, "Calibri", True)
        passou_por_op = True
        if mouse.is_over_object(botao_mult) and clique_confirmado:
            personagens = [flavia, wumberto]
            multiplayer, state = True, "jogando"
            vida_F, vida_W = 3, 1000
        if mouse.is_over_object(botao_volta) and clique_confirmado:
            state = "menu"
    elif state == "jogando":
        if not passou_por_op: 
            personagens = [wumberto]
        if multiplayer: vida_W = 1000

        if prox_fase <= 0:
            fase += 1
            if fase > 2: 
                state = "game_over"
                janela.draw_text("OBRIGADO POR ME SALVAR!!! COMPANHEIRO", janela.width/2 - 450, janela.height/2, 40, amarelo, "Calibri", True)
                PRESIDENTE.draw()
                janela.update(); janela.delay(3000); reset(); continue
            else:
                prox_fase = 20
                boss_spawnado = False
                mostrar_mensagem, timer_mensagem_fase = True, 0.0
                mensagem_fase_texto = f"FASE {fase + 1}"

        if fase == 0: Fundo.draw()
        elif fase == 1: Fundo2.draw()
        else: Fundo3.draw()

        timer += delta_time; frames += delta_time; n_frames += 1
        if frames >= 1: fps, frames, n_frames = n_frames/frames, 0.0, 0
        
        # Flavia
        if flavia in personagens:
            anda_generico(flavia, "W", "S", "A", "D", delta_time)
            HUB_F.draw()
            if teclado.key_pressed("space") and timer >= 0.5:
                timer = 0
                if fla_D: 
                    da_tiro_D(flavia)
                else: 
                    da_tiro_E(flavia)
        
        # Tiros Jogador
        for i in range(len(tirosD) - 1, -1, -1):
            tiro = tirosD[i]
            tiro.draw()
            tiro.x += vel_tiro * delta_time
            if tiro.x >= janela.width: tirosD.pop(i); continue
            for j in range(len(inis_ativos) -1 , -1, -1):
                if inis_ativos[j]["sprite_atual"].collided(tiro):
                    causar_dano_ini(j, 1); tirosD.pop(i); break#dá pra fazer que nem em C, Allan dá pra diminur os if só colocar ; mas se n quiser n precisa
        
        for i in range(len(tirosE) - 1, -1, -1):
            tiro = tirosE[i]
            tiro.draw()
            tiro.x -= vel_tiro * delta_time
            if tiro.x + tiro.width <= 0: tirosE.pop(i); continue
            for j in range(len(inis_ativos) -1 , -1, -1):
                if inis_ativos[j]["sprite_atual"].collided(tiro):
                    causar_dano_ini(j, 1); tirosE.pop(i); break

        # Wumberto
        if wumberto in personagens: 
            timerW += delta_time   
            anda_generico(wumberto, "UP", "DOWN", "LEFT", "RIGHT", delta_time)
            atualizar_animacao_wumberto()
            HUB_W.draw()
            
            if teclado.key_pressed("K"):
                if not Soco_Ativo:
                    Soco_Ativo, tempo_soco, soco_ja_hitou = True, 0.0, False
                
                if wum_D: Soco.set_position(wumberto.x + wumberto.width, wumberto.y + 30)
                else: Soco.set_position(wumberto.x - Soco.width, wumberto.y + 30)
            
            if teclado.key_pressed("L"):
                dando_tiro_w = True
                if timerW >= 0.5:
                    timerW = 0
                    if wum_D: 
                        da_tiro_D(wumberto)
                    else: 
                        da_tiro_E(wumberto)
            else: dando_tiro_w = False
            
        if Soco_Ativo:
            sons["soco"].play()
            tempo_soco += delta_time
            if tempo_soco >= DURACAO_SOCO: Soco_Ativo = False
            elif not soco_ja_hitou: 
                for i in range(len(inis_ativos)-1, -1, -1):
                    if Soco.collided(inis_ativos[i]["sprite_atual"]): 
                        causar_dano_ini(i, 1.5); soco_ja_hitou = True; break
        
        if teclado.key_pressed("D"): fla_D = True
        if teclado.key_pressed("A"): fla_D = False
        if teclado.key_pressed("RIGHT"): wum_D = True
        if teclado.key_pressed("LEFT"): wum_D = False

        tempo_passado += delta_time
        q_ativos = len(inis_ativos)
        
        # Spawns
        if fase == 2 and prox_fase <= 17 and not boss_spawnado: 
            chama_boss()
        if fase == 2 and prox_fase <= 17:
            vida_boss = 0
            for ini in inis_ativos:
                if ini.get("eh_boss"): vida_boss = ini["vida"]
            
            if vida_boss > 0: cheia.draw()
            else: vazia.draw()

            if vida_boss > 10: cheia2.draw()
            else: vazia2.draw()

            if vida_boss > 20: cheia3.draw()
            else: vazia3.draw()
        if q_ativos < total_tela:
            intervalo_spawn = 3.0 - (fase * 0.28)
            if (tempo_passado - ultimo_chamado) >= intervalo_spawn:
                chama_ini() 
                ultimo_chamado = tempo_passado
        
        gerenciar_inimigos(delta_time)
        
        # Frangos
        frangos_remov = []
        for idx, frango in enumerate(Frangos):
            frango.draw()
            if flavia in personagens and frango.collided(flavia):
                vida_F = min(vida_F + 1, 3); frangos_remov.append(idx)
            elif wumberto in personagens and frango.collided(wumberto):
                sons["vida"].play()
                vida_W = min(vida_W + 1, 3); frangos_remov.append(idx)
        for idx in sorted(frangos_remov, reverse=True): 
            Frangos.pop(idx)
        
        for p in personagens:
            n_sai_da_tela(p)
            if p == wumberto and wumberto.visivel: wumberto_atual.draw()
            elif p == flavia and flavia.visivel: p.draw()

        # HUD
        if flavia in personagens:
            c = vermelho if invencivel_flavia else branco
            janela.draw_text(f"♥ {vida_F}", janela.width - HUB_F.width - 100, 50, 50, c, "Calibri", True)
        if wumberto in personagens:
            c = vermelho if invencivel_wumberto else branco
            janela.draw_text(f"{vida_W} ♥", 130, 50, 50, c, "Calibri", True)
        
        if invencivel_flavia:
            janela.draw_text(f"INV: {duracao_invencibilidade-timer_invencibilidade_flavia:.1f}s", janela.width-130, 100, 20, amarelo, "Calibri", True)
        if invencivel_wumberto:
            janela.draw_text(f"INV: {duracao_invencibilidade-timer_invencibilidade_wumberto:.1f}s", 130, 100, 20, amarelo, "Calibri", True)

        if esc_atual and not esc_anterior: state = "pausa"
        
        janela.draw_text(f"FPS: {int(fps)}", 10, 10, 15, amarelo, bold=True)
        janela.draw_text(f"INIMIGOS: {int(prox_fase)}", janela.width/2 - 80, 20, 30, vermelho, "Calibri", True)

        if mostrar_mensagem:
            timer_mensagem_fase += delta_time
            janela.draw_text(mensagem_fase_texto, janela.width/2 - 100, janela.height/2 - 50, 60, preto, "Calibri", True)
            if timer_mensagem_fase >= duracao_mensagem: mostrar_mensagem = False
        
    elif state == "pausa":
        menu_pausa.draw()
        janela.draw_text("Esc: Voltar ao jogo", janela.width/2-100, janela.height/2+150, 20, branco, "Calibri", True)
        janela.draw_text("Espaço: Voltar ao menu", janela.width/2-120, janela.height/2+180, 20, branco, "Calibri", True)
        if esc_atual and not esc_anterior: state = "jogando"
        if teclado.key_pressed("SPACE"): state = "menu"
    
    elif state == "game_over":
        Fundo.draw()
        janela.draw_text("GAME OVER", janela.width/2 - 150, janela.height/2 - 50, 60, vermelho, "Calibri", True)
        janela.draw_text("Espaço para Menu", janela.width/2 - 120, janela.height/2 + 50, 30, branco, "Calibri", True)
        if teclado.key_pressed("SPACE"): 
            reset()
    
    esc_anterior = esc_atual
    janela.update()