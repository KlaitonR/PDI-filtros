package view;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import model.Pixel;

public class PDI {
	
	public static Image cinzaMediaAritmetica (Image imagem, int pcr, int pcg, int pcb) {
		
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
				
						Color corA = pr.getColor(i,j);
						double media = (corA.getRed()+corA.getGreen()+corA.getBlue())/3;
						
						if(pcr != 0 || pcg != 0 || pcb != 0)
							media = (corA.getRed()*pcr + corA.getGreen()*pcg +corA.getBlue()*pcb)/100;
						
						Color corN = new Color(media, media, media, corA.getOpacity());
						pw.setColor(i, j, corN);
				
				}
			}
				
			return wi;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static Image limiarizacao(Image imagem, double limiar) {
		
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w; i++) {
				for(int j=0;j<h;j++) {
					Color corA = pr.getColor(i, j);
					Color corN;
					
					if(corA.getRed() >= limiar)
						corN = new Color (1,1,1, corA.getOpacity());
					else
						corN = new Color(0,0,0, corA.getOpacity());
					
					pw.setColor(i, j, corN);
				}
			}
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image negativa(Image imagem){
		
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w; i++) {
				for(int j=0;j<h;j++) {
					Color corA = pr.getColor(i,j);
					Color corN = new Color(
							1 - corA.getRed(),
							1 - corA.getGreen(),
							1 - corA.getBlue(),
							corA.getOpacity());
					pw.setColor(i, j, corN);
				}
			}	
			return wi;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image ruidos(Image imagem, int tipoVizinho) {
		
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			for (int i = 1; i < w-1; i++) {
				for (int j = 1; j < h-1; j++) {
					
					Color corA = pr.getColor(i, j);
					Pixel p = new Pixel(corA.getRed(), corA.getGreen(), corA.getBlue(), i, j);
					buscaVizinhos(imagem, p);
					Pixel vetor[] = null;
					
					if(tipoVizinho == 1) 
						vetor = p.viz3;
					if(tipoVizinho == 2) 
						vetor = p.vizC;
					if(tipoVizinho == 3) 
						vetor = p.vizX;
					
					double red = mediana(vetor,1);
					double green = mediana(vetor,2);
					double blue = mediana(vetor,3);
					
					Color corN = new Color(red,green,blue,corA.getOpacity());
					pw.setColor(i, j, corN);
					
				}
			}
			
		return wi;
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void buscaVizinhos(Image imagem, Pixel p) {
		p.vizX = buscaVizinhosX(imagem,p);
		p.vizC = buscaVizinhosC(imagem,p);
		p.viz3 = buscaVizinhos3(imagem,p);
	}
	
	public static Pixel[] buscaVizinhosX(Image imagem, Pixel p) {
		Pixel[] vX = new Pixel[5];
		PixelReader pr = imagem.getPixelReader();
		
		Color cor = pr.getColor(p.x-1, p.y+1);
		vX[0] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y+1);
		cor = pr.getColor(p.x+1, p.y-1);
		vX[1] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y-1);
		cor = pr.getColor(p.x-1, p.y-1);
		vX[2] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y-1);
		cor = pr.getColor(p.x+1, p.y+1);
		vX[3] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y+1);
		vX[4] = p;
		
		return vX;
	}
	
	public static Pixel[] buscaVizinhosC(Image imagem, Pixel p) {
		Pixel[] vC = new Pixel[5];
		PixelReader pr = imagem.getPixelReader();
		
		Color cor = pr.getColor(p.x, p.y+1);
		vC[0] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x,p.y+1);
		cor = pr.getColor(p.x, p.y-1);
		vC[1] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x,p.y-1);
		cor = pr.getColor(p.x-1, p.y);
		vC[2] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y);
		cor = pr.getColor(p.x+1, p.y);
		vC[3] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y);
		vC[4] = p;
		
		return vC;
	}
	
	public static Pixel[] buscaVizinhos3(Image imagem, Pixel p) {
		Pixel[] v3 = new Pixel[9];
		PixelReader pr = imagem.getPixelReader();
		
		Color cor = pr.getColor(p.x, p.y+1);
		v3[0] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x,p.y+1);
		cor = pr.getColor(p.x, p.y-1);
		v3[1] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x,p.y-1);
		cor = pr.getColor(p.x-1, p.y);
		v3[2] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y);
		cor = pr.getColor(p.x+1, p.y);
		v3[3] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y);
		cor = pr.getColor(p.x-1, p.y+1);
		v3[4] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y+1);
		cor = pr.getColor(p.x+1, p.y-1);
		v3[5] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y-1);
		cor = pr.getColor(p.x-1, p.y-1);
		v3[6] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y-1);
		cor = pr.getColor(p.x+1, p.y+1);
		v3[7] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y+1);
		v3[8] = p;
		
		return v3;
	}
	
	public static double mediana(Pixel[] pixels, int canal) {
		
		double v[] = new double[pixels.length];
		
		for (int i = 0; i < pixels.length; i++) {
			
			if(canal == 1) 
				v[i] = pixels[i].r;
			if(canal == 2) 
				v[i] = pixels[i].g;
			if(canal == 3) 
				v[i] = pixels[i].b;
			
		}
		
		Arrays.sort(v);
		return v[v.length/2];
	}
	
	public static Image adicao(Image img1, Image img2, double ti1, double ti2) {
		
		int w1 = (int)img1.getWidth();
		int h1 = (int)img1.getHeight();
		int w2 = (int)img2.getWidth();
		int h2 = (int)img2.getHeight();
		
		int w = Math.min(w1, w2);
		int h = Math.min(h1, h2);
		
		PixelReader pr1 = img1.getPixelReader();
		PixelReader pr2 = img2.getPixelReader();
		WritableImage wi = new WritableImage(w,h);
		PixelWriter pw = wi.getPixelWriter();
		
		for(int i=1; i<w; i++) {
			for(int j=1; j<h; j++) {
				Color corImg1 = pr1.getColor(i, j);
				Color corImg2 = pr2.getColor(i, j);
				double r = (corImg1.getRed()*ti1) + (corImg2.getRed()*ti2);
				double g = (corImg1.getGreen()*ti1) + (corImg2.getGreen()*ti2);
				double b = (corImg1.getBlue()*ti1) + (corImg2.getBlue()*ti2);
				
				r = r>1 ? 1 : r;
				g = g>1 ? 1 : g;
				b = b>1 ? 1 : b;
				
				Color newCor = new Color(r,g,b,1);
				pw.setColor(i, j, newCor);
			}
		}
		return wi;
	}
	
	public static Image subtracao(Image img1, Image img2) {
		
		int w1 = (int)img1.getWidth();
		int h1 = (int)img1.getHeight();
		int w2 = (int)img2.getWidth();
		int h2 = (int)img2.getHeight();
		
		int w = Math.min(w1, w2);
		int h = Math.min(h1, h2);
		
		PixelReader pr1 = img1.getPixelReader();
		PixelReader pr2 = img2.getPixelReader();
		WritableImage wi = new WritableImage(w,h);
		PixelWriter pw = wi.getPixelWriter();
		
		for(int i=1; i<w; i++) {
			for(int j=1; j<h; j++) {
				Color oldCor1 = pr1.getColor(i, j);
				Color oldCor2 = pr2.getColor(i, j);
				double r = (oldCor1.getRed()) - (oldCor2.getRed());
				double g = (oldCor1.getGreen()) - (oldCor2.getGreen());
				double b = (oldCor1.getBlue()) - (oldCor2.getBlue());
				
				r = r<0 ? 0 : r;
				g = g<0 ? 0 : g;
				b = b<0 ? 0 : b;
				
				Color newCor = new Color(r,g,b,oldCor1.getOpacity());
				pw.setColor(i, j, newCor);
			}
		}
		return wi;
	}

	public static Image marcacao(Image img, int xi, int yi, int xf, int yf) {
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w,h);
		PixelWriter pw = wi.getPixelWriter();
		
		Color corNova = new Color(1,0,0,1);
		
		for(int i=0; i<w; i++) {
			for(int j=0; j<h; j++) {
				pw.setColor(i, j, pr.getColor(i, j));	
			}
		}
		    
		for(int x=xi; x<=xf; x++) {
			pw.setColor(x,yi , corNova);
			pw.setColor(x, yi+1, corNova); //aumentar espessura da borda
			pw.setColor(x, yi-1, corNova);
		}
			
		for(int x=xi; x<=xf; x++) {
			pw.setColor(x,yf , corNova);	
			pw.setColor(x, yf+1, corNova);
			pw.setColor(x, yf-1, corNova);
		}
		
		for(int y=yi; y<=yf; y++) {
			pw.setColor(xi,y , corNova);
			pw.setColor(xi+1, y, corNova);
			pw.setColor(xi-1, y, corNova);
		}
		
		for(int y=yi; y<=yf; y++) {
			pw.setColor(xf,y , corNova);	
			pw.setColor(xf+1, y, corNova); 
			pw.setColor(xf-1, y, corNova);
		}
		
		return wi;
	
		}
	
		@SuppressWarnings({"rawtypes","unchecked"})
		public static void getGrafico(Image img, BarChart<String, Number> grafico) {
		
			int[] hist = histogramaUnico(img);
			//int[] histR = histograma(img,1);
			//int[] histG = histograma(img,2);
			//int[] histB = histograma(img,3);
			
			XYChart.Series vlr =  new XYChart.Series();
			//XYChart.Series vlrR =  new XYChart.Series();
			//XYChart.Series vlrG =  new XYChart.Series();
			//XYChart.Series vlrB =  new XYChart.Series();
			
			
			for(int i=0; i<hist.length; i++) {
				vlr.getData().add(new XYChart.Data(i + "", hist[i]));		
				//vlrR.getData().add(new XYChart.Data(i + "", histR[i]));			
				//vlrG.getData().add(new XYChart.Data(i + "", histG[i]));			
				//vlrB.getData().add(new XYChart.Data(i + "", histB[i]));			
			}
			
			grafico.getData().addAll(vlr);
			
			for(Node n:grafico.lookupAll(".default-color0.chart-bar")) {
				n.setStyle("-fx-bar-fill: red;");
			}
			
			/*for(Node n:grafico.lookupAll(".default-color0.chart-bar")) {
				n.setStyle("-fx-bar-fill: green;");
			}
			
			for(Node n:grafico.lookupAll(".default-color0.chart-bar")) {
				n.setStyle("-fx-bar-fill: blue;");
			}*/
		}
		
		
		
		public static int[] histogramaUnico (Image img) {
			
			int[] qt = new int [256];
			PixelReader pr = img.getPixelReader();
			int w = (int)img.getWidth();
			int h = (int)img.getHeight();
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					qt[(int)(pr.getColor(i, j).getRed()*255)]++;
					qt[(int)(pr.getColor(i, j).getGreen()*255)]++;
					qt[(int)(pr.getColor(i, j).getBlue()*255)]++;
				}
			}
			
			return qt;
		}
		
		public static int[] histograma(Image img, int cor) {
			
			int[] qt = new int[256];
			PixelReader pr = img.getPixelReader();

			int w = (int)img.getWidth();
			int h = (int)img.getHeight();

			if (cor == 1) {
				for (int i = 0; i < w; i++) {
					for (int j = 0; j < h; j++) {
						qt[(int)(pr.getColor(i, j).getRed()*255)]++;
					}
				}
			}else if (cor == 2) {
				for (int i = 0; i < w; i++) {
					for (int j = 0; j < h; j++) {
						qt[(int)(pr.getColor(i, j).getGreen()*255)]++;
					}
				}
			}else if (cor == 3) {
				for (int i = 0; i < w; i++) {
					for (int j = 0; j < h; j++) {
						qt[(int)(pr.getColor(i, j).getBlue()*255)]++;
					}
				}
			}
			return qt;		
		}
		
		 private static int[] histogramaAcumulado(int[] VetorAux) {
			 
		 		int total = 0;
		 		int[] histogramAc = new int[VetorAux.length];	
		 		
		 		for (int i = 0; i < histogramAc.length; i++) {			
		 			histogramAc[i] = total + VetorAux[i];
		 			total = histogramAc[i];
		 		}
		 		return histogramAc;
		 	}
		
		private static int qtTons(int[] vetor) {
			int qt = 255;
			for (int i = 0; i < vetor.length; i++) {
				if (vetor[i] == 0) {
					qt--;				
				}
			}
			return qt;
	   	}
		
		public static int pontoMin(int[] hist) {
			
			for(int i=0; i<hist.length; i++) {
				if(hist[i] > 0)
					return i;
			}
			
			return 0;
		}
		
		public static Image equalizacaoHistograma(Image img, boolean todos) {
			
			int w = (int) img.getWidth();
			int h = (int) img.getHeight();

			PixelReader pr = img.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();

			int[] hR = histograma(img, 1);
			int[] hG = histograma(img, 2);
			int[] hB = histograma(img, 3);

			int[] histAcR = histogramaAcumulado(hR);
			int[] histAcG = histogramaAcumulado(hG);
			int[] histAcB = histogramaAcumulado(hB);

			int qtTonsRed = qtTons(hR);
			int qtTonsGreen = qtTons(hG);
			int qtTonsBlue = qtTons(hB);

			double minR = pontoMin(hR);
			double minB = pontoMin(hB);
			double minG = pontoMin(hG);

			if (todos) {
				qtTonsRed = 255;
				qtTonsGreen = 255;
				qtTonsBlue = 255;
				minR = 0;
				minG = 0;
				minB = 0;
			}
			
			double n = w*h;
			
			for (int i = 1; i < w; i++) {
				for (int j = 1; j < h; j++) {
					Color oldColor = pr.getColor(i, j);

					double acR = histAcR[(int) (oldColor.getRed() * 255)];
					double acG = histAcG[(int) (oldColor.getGreen() * 255)];
					double acB = histAcB[(int) (oldColor.getBlue() * 255)];

					double pxR = ((qtTonsRed - 1) / n) * acR;
					double pxG = ((qtTonsGreen - 1) / n) * acG;
					double pxB = ((qtTonsBlue - 1) / n) * acB;

					double corR = (minR + pxR) / 255;
					double corG = (minG + pxG) / 255;
					double corB = (minB + pxB) / 255;

					Color newCor = new Color(corR, corG, corB, oldColor.getOpacity());
					pw.setColor(i, j, newCor);
				}	
			}
			
			return wi;
		}
		
		
		//------------------------- CÓDIGOS PROVA ------------------------
		
		
		//------------------------- Questão 1 ------------------------	
		public static Image inverte(Image imgagem, boolean quad1, boolean quad2, boolean quad3, boolean quad4) {
			
	        int w = (int)imgagem.getWidth();
	        int h = (int)imgagem.getHeight();
	        
	        int divisaHorizontal = w/2; //divide em quadrantes
			int divisaVertical = h/2;
			
			ArrayList<Color> quadrante1 = new ArrayList<>(); //Vetores para inverter a imagem
			ArrayList<Color> quadrante2 = new ArrayList<>();
			ArrayList<Color> quadrante3 = new ArrayList<>();
			ArrayList<Color> quadrante4 = new ArrayList<>();

	        PixelReader pr = imgagem.getPixelReader();
	        WritableImage wi = new WritableImage(w, h);
	        PixelWriter pw = wi.getPixelWriter();

	        for (int i = 0; i < w; i++) { // Deixa a imagem normal
	            for (int j = 0; j < h; j++) {
	            	Color corA = pr.getColor(i, j);
	            	pw.setColor(i, j, corA);
	            }
	        }
	        
	        if (quad1 == true) { //gira quadrante 1
	        	
	            for (int i = 0; i < divisaHorizontal; i++) {
	                for (int j = 0; j < divisaVertical; j++) {
	                	
	                	quadrante1.add(pr.getColor(i, j));  //adiciona os pixels
	                } 
	            }

	            int cont = 0;
	            
	            for (int i = (divisaHorizontal)-1; i > 0; i--) {
	                for (int j = (divisaVertical)-1; j > 0; j--) {
	                	
	                    pw.setColor(i, j, quadrante1.get(cont));
	                    cont++;
	                }
	                
	                cont++;
	            }
	        }

	     
	        if (quad2 == true) { //gira quadrante 2
	        	
	            for (int i = divisaHorizontal; i < h; i++) {
	                for (int j = 0; j < divisaVertical; j++) {
	                	
	                	quadrante2.add(pr.getColor(i, j));  //adiciona os pixels
	                	
	                }
	            }

	            int cont = 0;
	            
	            for (int i = w-1; i > (divisaHorizontal)-1; i--) {
	                for (int j = (divisaVertical)-1; j > 0; j--) {
	                	
	                    pw.setColor(i, j, quadrante2.get(cont));
	                    cont++;
	                    
	                }
	                
	                cont++;
	            }
	        }

	      
	        if (quad3 == true) { //gira quadrante 3
	 
	            for (int i = 0; i < divisaHorizontal; i++) {
	                for (int j = divisaVertical; j < h; j++) {
	                	
	                    quadrante3.add(pr.getColor(i, j));  //adiciona os pixels
	                    
	                }
	            }

	            int cont = 0;
	            for (int i = (divisaHorizontal)-1; i > 0; i--) {
	                for (int j = h-2; j > (divisaVertical)-1; j--) {
	                	
	                    pw.setColor(i, j, quadrante3.get(cont));
	                    cont++;
	                }
	                cont++;
	            }
	        }

	    
	        if (quad4 == true) { //gira quadrante 4
	 
	            for (int i = divisaHorizontal; i < w; i++) {
	                for (int j = divisaVertical; j < h; j++) {
	                	
	                    quadrante4.add(pr.getColor(i, j)); //adiciona os pixels
	                    
	                }
	            }

	            int cont = 0;
	            
	            for (int i = w-1; i > (divisaHorizontal)-1; i--) { 
	                for (int j = h-2; j > (divisaVertical)-1; j--) {
	                	
	                    pw.setColor(i, j, quadrante4.get(cont));
	                    cont++;
	                    
	                }
	                
	                cont++;
	                
	            }
	        }
	        
	        return wi;

	    }
		
		//------------------------- Questão 2 ------------------------	
	public static Image equalizaNaDiagonal(Image img, boolean todos) {
			
			int w = (int) img.getWidth();
			int h = (int) img.getHeight();

			PixelReader pr = img.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();

			int[] hR = histograma(img, 1);
			int[] hG = histograma(img, 2);
			int[] hB = histograma(img, 3);

			int[] histAcR = histogramaAcumulado(hR);
			int[] histAcG = histogramaAcumulado(hG);
			int[] histAcB = histogramaAcumulado(hB);

			int qtTonsRed = qtTons(hR);
			int qtTonsGreen = qtTons(hG);
			int qtTonsBlue = qtTons(hB);

			double minR = pontoMin(hR);
			double minB = pontoMin(hB);
			double minG = pontoMin(hG);

			if (todos) {
				qtTonsRed = 255;
				qtTonsGreen = 255;
				qtTonsBlue = 255;
				minR = 0;
				minG = 0;
				minB = 0;
			}
			
			double n = w*h;
			
			for (int i = 1; i < w; i++) {
				for (int j = 1; j < h; j++) {
					
					Color oldColor = pr.getColor(i, j);
					Color black = new Color(0, 0, 0, 1);
					
					if(i==j)  //diagonal
						pw.setColor(i, j, black);
					
					if(i>j) { // filtro de equalização

						double acR = histAcR[(int) (oldColor.getRed() * 255)];
						double acG = histAcG[(int) (oldColor.getGreen() * 255)];
						double acB = histAcB[(int) (oldColor.getBlue() * 255)];
						
						double pxR = ((qtTonsRed - 1) / n) * acR;
						double pxG = ((qtTonsGreen - 1) / n) * acG;
						double pxB = ((qtTonsBlue - 1) / n) * acB;

						double corR = (minR + pxR) / 255;
						double corG = (minG + pxG) / 255;
						double corB = (minB + pxB) / 255;

						Color newCor = new Color(corR, corG, corB, oldColor.getOpacity());
						pw.setColor(i, j, newCor);
							
					}
					
					if(i<j) //sem filtro
						pw.setColor(i, j, oldColor);
			
				}
			}
			
			return wi;
		}
	
	//------------------------- Questão 3 ------------------------	
	public static String quadrados(Image imagem) {
		
		try {
			
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			int contVizinhosPretos = 0;
			String resultado = "";
			boolean verificaResultado = false;
			
			PixelReader pr = imagem.getPixelReader();
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					
					contVizinhosPretos = 0;
					Color corA = pr.getColor(i,j);
			
					if(corA.getRed() == 0 || corA.getBlue() == 0 || corA.getGreen() == 0) { // verifica se é preto
						
						Pixel p = new Pixel(corA.getRed(), corA.getBlue(), corA.getGreen(), i, j);
						buscaVizinhos(imagem, p);
						
						Pixel vetor[] = null;
						vetor = p.vizC; 
						
						for(int k=0; k<vetor.length;k++) {  // Soma a quantia de vizinhos pretos
							if(vetor[k].r == 0 || vetor[k].b == 0 || vetor[k].g == 0) {
								contVizinhosPretos++;
							}
						}
						
						if(contVizinhosPretos<=2) //verifica se tem 2 vizinhos pretos, casso não tenha 2, o quadrado está aberto 
							verificaResultado = true; //(no caso 3, pois o próprio pixel em analise é adicionado no vetor)
						
					}
					
				}
			}
			
			if(verificaResultado == true) //// verifica se tem 2 vizinhos pretos, casso não tenha 2, o quadrado está aberto
				resultado = "O quadrado está aberto";
			else 
				resultado = "O quadrado está fechado";
				
			return resultado;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
			
}
	

