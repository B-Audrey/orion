import { ChangeDetectionStrategy, Component, AfterViewInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'mdd-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatButtonModule],
})
export class HomeComponent implements AfterViewInit {
  ngAfterViewInit(): void {
    const anecdotes: string[] = [
      "La première carte de Noël a été imprimée en 1843 à Londres, la même année que la publication du 'Chant de Noël' de Charles Dickens.",
      "Le Père Noël rouge et blanc tel qu'on le connaît aujourd'hui a été popularisé par Coca-Cola dans les années 1930.",
      "En France, la messe de minuit est une tradition ancienne, souvent suivie d'un grand repas appelé le 'réveillon'.",
      "Dans le sud de la France, on sert traditionnellement les '13 desserts' de Noël en hommage à Jésus et aux 12 apôtres.",
      "En Provence, la crèche de Noël est très importante, avec ses 'Santons' représentant les habitants du village.",
      "En Alsace, les marchés de Noël existent depuis le XVIe siècle, notamment celui de Strasbourg, le plus ancien d'Europe.",
      "La bûche de Noël était à l'origine un vrai tronc brûlé dans la cheminée avant de devenir le célèbre dessert roulé.",
      'Dans certaines régions françaises, on laisse encore une part du repas de Noël pour le passage du petit Jésus.',
      'Le sapin de Noël a été introduit en France au XVIe siècle en Alsace, alors territoire du Saint-Empire romain germanique.',
      'En Bretagne, il était coutume de placer une bougie à la fenêtre pour guider les voyageurs la nuit de Noël.',
      "En Lorraine, c'est Saint Nicolas, le 6 décembre, qui distribue les cadeaux aux enfants sages, avant le Père Noël.",
      'La dinde de Noël est apparue sur les tables françaises après sa découverte en Amérique au XVIe siècle.',
      "Dans certaines familles françaises, le repas du réveillon dure plusieurs heures et se termine souvent vers l'aube.",
      "Le mot 'Noël' vient du latin 'natalis', qui signifie 'naissance'.",
      "Le mot 'réveillon' vient de 'réveiller', car on restait éveillé tard pour fêter la naissance du Christ.",
      "Les rennes du Père Noël ont été cités pour la première fois dans un poème de 1823 intitulé 'A Visit from St. Nicholas'.",
      'En 1914, une trêve de Noël a eu lieu pendant la Première Guerre mondiale : soldats allemands et français ont échangé des cadeaux.',
      'À Paris, les vitrines des grands magasins se transforment chaque année en véritables spectacles de Noël animés.',
      "La tradition d'offrir des mandarines à Noël vient de l'époque où ces fruits étaient rares et précieux en hiver.",
      "La 'galette des rois' est parfois consommée dès la fin des fêtes de Noël, bien qu'elle célèbre l'Épiphanie du 6 janvier.",
      'En Corse, on allumait autrefois un grand feu dans le village pour rassembler toute la communauté à Noël.',
      'En Savoie, la fondue ou la raclette remplacent souvent la dinde lors du repas du réveillon.',
      'Dans le Nord, la Saint-Nicolas reste une fête aussi importante que Noël pour les enfants.',
      'En Champagne, on débouche évidemment du champagne à Noël, symbole de fête et de partage.',
      "En Auvergne, on servait autrefois le 'cochon de Noël', un grand repas partagé entre voisins et familles.",
      "Le plus grand marché de Noël de France se trouve à Strasbourg, surnommée 'la capitale de Noël'.",
      "En Provence, on chante encore parfois les 'pastorales', des pièces de théâtre populaires sur la nativité.",
      "La chanson 'Petit Papa Noël' de Tino Rossi, sortie en 1946, est encore aujourd'hui la plus célèbre chanson de Noël française.",
      'Dans certaines régions, on cache une fève dans la bûche de Noël, une tradition inspirée de la galette des rois.',
      'Les décorations de Noël en France incluent souvent des guirlandes lumineuses et des étoiles pour rappeler celle de Bethléem.',
      "En montagne, la neige donne lieu à de véritables Noëls féeriques avec feux d'artifice, descentes aux flambeaux et chants traditionnels.",
      'À Lyon, la Fête des Lumières coïncide souvent avec la période de Noël et illumine toute la ville.',
      'Le Père Noël descend traditionnellement par la cheminée pour déposer les cadeaux dans les souliers au pied du sapin.',
      "En Alsace, on accroche souvent des petits biscuits 'bredeles' au sapin avant de les déguster le soir du réveillon.",
      "La chanson 'Vive le vent' est l'adaptation française de 'Jingle Bells', écrite par Francis Blanche en 1948.",
      "Les Français décorent souvent leur maison dès le début du mois de décembre, et le sapin est souvent retiré à l'Épiphanie.",
      "En France, on offre souvent des chocolats ou des papillotes, ces bonbons entourés d'un papier doré contenant un message.",
      'Les crèches vivantes, où les habitants rejouent la nativité, sont encore populaires dans de nombreux villages français.',
    ];

    const randomAnecdote = anecdotes[Math.floor(Math.random() * anecdotes.length)];
    const target = document.getElementById('app-logo-img');
    if (!target) {
      return;
    }
    const targetRect = target.getBoundingClientRect();
    const offsetX = 16; // account for left padding

    // Build a flip card with inline sizing to avoid full-width image
    const floating = document.createElement('div');
    floating.style.position = 'fixed';
    floating.style.left = '0px';
    floating.style.top = '0px';
    floating.style.width = '180px';
    floating.style.height = '320px';
    floating.style.zIndex = '10000';
    floating.style.pointerEvents = 'none';
    floating.style.perspective = '800px';
    floating.style.transformOrigin = 'top left';

    const inner = document.createElement('div');
    inner.style.position = 'relative';
    inner.style.width = '100%';
    inner.style.height = '100%';
    inner.style.transformStyle = 'preserve-3d';
    inner.style.transition = 'transform 600ms ease';

    const front = document.createElement('div');
    front.style.position = 'absolute';
    front.style.inset = '0';
    (front.style as CSSStyleDeclaration & { backfaceVisibility?: string }).backfaceVisibility =
      'hidden';
    front.style.borderRadius = '3px';
    front.style.overflow = 'hidden';
    front.style.boxShadow = '0 6px 20px rgba(0,0,0,0.25)';

    const img = document.createElement('img');
    img.src = 'assets/img/lutins.png';
    img.alt = 'Noel';
    img.style.width = '100%';
    img.style.height = '100%';
    img.style.objectFit = 'contain';
    front.appendChild(img);

    const back = document.createElement('div');
    back.style.position = 'absolute';
    back.style.inset = '0';
    (back.style as CSSStyleDeclaration & { backfaceVisibility?: string }).backfaceVisibility =
      'hidden';
    back.style.borderRadius = '3px';
    back.style.overflow = 'hidden';
    back.style.boxShadow = '0 6px 20px rgba(0,0,0,0.25)';
    back.style.background = '#0a230c';
    back.style.color = '#ffffff';
    back.style.display = 'flex';
    back.style.alignItems = 'center';
    back.style.justifyContent = 'center';
    back.style.padding = '12px';
    back.style.textAlign = 'center';
    back.style.fontWeight = '200';
    back.style.fontSize = '12px';
    (back.style as CSSStyleDeclaration & { transform?: string }).transform = 'rotateY(180deg)';
    back.textContent = randomAnecdote;

    inner.appendChild(front);
    inner.appendChild(back);
    floating.appendChild(inner);
    document.body.appendChild(floating);

    // compute start center based on actual image size after it loads/layouts
    requestAnimationFrame(() => {
      const cardRect = floating.getBoundingClientRect();
      const startX = window.innerWidth / 2 - cardRect.width / 2;
      const startY = window.innerHeight / 2 - cardRect.height / 2;
      const endScale = Math.max(0.1, targetRect.width / cardRect.width);

      // Immediately show at center
      floating.style.transform = `translate(${startX}px, ${startY}px) scale(1)`;
      floating.style.willChange = 'transform';

      // Flip to show the back at 2s
      setTimeout(() => {
        inner.style.transform = 'rotateY(180deg)';
      }, 2000);

      // Move to logo at 4s total
      setTimeout(() => {
        const animation = floating.animate(
          [
            { transform: `translate(${startX}px, ${startY}px) scale(1)`, opacity: 1 },
            {
              transform: `translate(${targetRect.left + offsetX}px, ${targetRect.top}px) scale(${endScale})`,
              opacity: 0.1,
            },
          ],
          { duration: 2000, easing: 'ease-in-out' },
        );
        animation.onfinish = () => {
          // snap to target and remove
          floating.style.transform = `translate(${targetRect.left + offsetX}px, ${targetRect.top}px) scale(${endScale})`;
          floating.remove();
        };
      }, 6000);
    });
  }
}
