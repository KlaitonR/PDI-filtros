package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PrincipalController {
	
	@FXML ImageView imageView1;
	@FXML ImageView imageView2;
	@FXML ImageView imageView3;
	
	@FXML Label lblR;
	@FXML Label lblB;
	@FXML Label lblG;
	
	@FXML TextField prR;
	@FXML TextField prG;
	@FXML TextField prB;
	
	@FXML Slider slider;
	@FXML Slider sliderTi1;
	@FXML Slider sliderTi2;
	
	@FXML RadioButton vizX;
	@FXML RadioButton viz3;
	@FXML RadioButton vizC;
	
	private Image img1;
	private Image img2;
	private Image img3;
	
	private int R;
	private int G;
	private int B;
	
	private double valSlider;
	private double ti1;
	private double ti2;
	
	int xi;
	int yi;
	int xf;
	int yf;
	
	//-------------------------- PROVA
	
		@FXML RadioButton quad1;
		@FXML RadioButton quad2;
		@FXML RadioButton quad3;
		@FXML RadioButton quad4;
		
		@FXML TextArea result;
	
		private String resultado;
		private boolean quadrante1 = false;
		private boolean quadrante2 = false;
		private boolean quadrante3 = false;
		private boolean quadrante4 = false;
	
	@FXML
	 public void abreImagem1() {
		img1 = abreImagem(imageView1, img1);
	}
	
	@FXML
	 public void abreImagem2() {
		img2 = abreImagem(imageView2, img2);
	}
	
	@FXML
	public void cinzaAritmetica() {
		
		img3 = PDI.cinzaMediaAritmetica(img1, 0, 0, 0);
		atualizaImagem3();
	}
	
	@FXML
	public void cinzaPonderado() {
		try {
		
			R = Integer.parseInt(prR.getText());
			G = Integer.parseInt(prG.getText());
			B = Integer.parseInt(prB.getText());
			
			/* if((R+G+B)>100) 
					throw new NumberFormatException("A porcentagem ultrapassou 100%");
			*/
			
			img3 = PDI.cinzaMediaAritmetica(img1, R, G, B);
			atualizaImagem3();
			
		}catch (NumberFormatException e) {
				mostraMensagem("Insira apenas números", AlertType.ERROR);
		}
			
		catch (Exception e) {
			mostraMensagem("Erro", AlertType.ERROR);
		}
	}
	
	@FXML 
	public void limiarizacao() {
		valSlider = slider.getValue();
		valSlider = valSlider/250;
		img3 = PDI.limiarizacao(img1, valSlider);
		atualizaImagem3();
	}
	
	@FXML
	public void negativa() {
		
		img3 = PDI.negativa(img1);
		atualizaImagem3();
	}
	
	@FXML 
	public void ruido() {
		
		int vizinho = 0;
		
		if (viz3.isSelected()) 
			vizinho = 1;
		else 
			if (vizC.isSelected())
				vizinho = 2;
			else
				if (vizX.isSelected())
					vizinho = 3;
		
		img3 = PDI.ruidos(img1, vizinho);
		atualizaImagem3();
	}
	
	@FXML 
	public void adicao() {
		
		ti1 = sliderTi1.getValue();
		ti1 = ti1/250;
		
		ti2 = sliderTi2.getValue();
		ti2 = ti2/250;
		
		img3 = PDI.adicao(img1, img2, ti1, ti2);
		
		atualizaImagem3();	
	}
	
	@FXML 
	public void subtracao() {
		
		ti1 = sliderTi1.getValue();
		ti1 = ti1/250;
		
		ti2 = sliderTi2.getValue();
		ti2 = ti2/250;
		
		img3 = PDI.subtracao(img1, img2);
		
		atualizaImagem3();
	}
	
	@FXML
	public void equalizacao() {
		img3 = PDI.equalizacaoHistograma(img1, false);
		atualizaImagem3();
	}
	
	@FXML
	public void equalizacaoValidos() {
		img3 = PDI.equalizacaoHistograma(img1, true);
		atualizaImagem3();
	}
	
	@FXML
	 public void inverte() {
		
		if (quad1.isSelected()) {
			quadrante1 = true;
		}
		
		if (quad2.isSelected()) {
			quadrante2 = true;
		}
		
		if (quad3.isSelected()) {
			quadrante3 = true;
		}
		
		if (quad4.isSelected()) {
			quadrante4 = true;
		}

		img3 = PDI.inverte(img1, quadrante1, quadrante2, quadrante3, quadrante4);
	}
	
	@FXML
	public void equalizaDiagonal() {
		img3 = PDI.equalizaNaDiagonal(img1, true);
		atualizaImagem3();
	}
	
	@FXML
	public void quadrados() {
		resultado = PDI.quadrados(img1);
		result.setText(resultado);
	}

	
	@FXML
	public void canny() {
		try {
			carregaOpenCV();

			Mat matImgDst = new Mat();
			Mat matImgSrc = Imgcodecs.imread("C:\\Users\\klait\\Desktop\\PDI\\imgs\\parafusos-2.jpg");

			Imgproc.Canny(matImgSrc, matImgDst, 150, 150);
			Imgcodecs.imwrite("C:\\Users\\klait\\Desktop\\PDI\\imgs\\canny.jpg", matImgDst);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void sobel() {
		try {
			carregaOpenCV();

			Mat matImgDst = new Mat();
			Mat matImgSrc = Imgcodecs.imread("C:\\Users\\klait\\Desktop\\PDI\\imgs\\parafusos-2.jpg");

			Imgproc.Sobel(matImgSrc, matImgDst, CvType.CV_16S, 1, 0);
			Imgcodecs.imwrite("C:\\Users\\klait\\Desktop\\PDI\\imgs\\sobel.jpg", matImgDst);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void laPlace() {
		try {
			carregaOpenCV();
			 
			Mat matImgDst = new Mat();
			Mat matImgSrc = Imgcodecs.imread("C:\\Users\\klait\\Desktop\\PDI\\imgs\\parafusos-2.jpg");

			Imgproc.Laplacian(matImgSrc,matImgDst, CvType.CV_64F);
			Imgcodecs.imwrite("C:\\Users\\klait\\Desktop\\PDI\\imgs\\laplace.jpg", matImgDst);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void carregaOpenCV() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	@FXML
	public void prewitt() {

		try {
			carregaOpenCV();

			Mat matImgDst = new Mat();
			;
			Mat matImgSrc = Imgcodecs.imread("C:\\Users\\klait\\Desktop\\PDI\\imgs\\canny.jpg");
			int kernelSize = 9;

			Mat kernel = new Mat(kernelSize, kernelSize,  CvType.CV_16SC1) {
				{
					put(0, 0, 0);
					put(-1, 0, 1);
					put(-1, 0, 1);

					put(1, 1, 1);
					put(0, 0, 0);
					put(-1, -1, -1);

					put(1, 0, 0);
					put(0, 1, 0);
					put(0, -1, -1);
				}
			};

			Imgproc.filter2D(matImgSrc, matImgDst, -1, kernel);
			Imgcodecs.imwrite("C:\\Users\\klait\\Desktop\\PDI\\imgs\\prewiit.jpg", matImgDst);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML
	public void MarcacaoMousePressed(MouseEvent evento) {
		
		ImageView img = (ImageView)evento.getTarget();
		if(img.getImage()!=null) {
			 xi = (int)evento.getX();
			 yi = (int)evento.getY();
		}
	}
	
	@FXML 	
	public void MarcacaoMouseRelease(MouseEvent evento) {
		
		ImageView img = (ImageView)evento.getTarget();
		if(img.getImage()!=null) {
			 xf = (int)evento.getX();
			 yf = (int)evento.getY(); 
			 img.setImage(PDI.marcacao(img3, Math.min(xi, xf), Math.min(yi, yf), Math.max(xi, xf), Math.max(yi, yf)));				
		}
		
	}
	
	private void verificaCor(Image img, int x, int y){
		  try {
				Color cor = img.getPixelReader().getColor(x-1, y-1);
				lblR.setText("R: "+(int) (cor.getRed()*255));
				lblG.setText("G: "+(int) (cor.getGreen()*255));
				lblB.setText("B: "+(int) (cor.getBlue()*255));
			} catch (Exception e) {
				//e.printStackTrace();
			}
	  }
	
	 @FXML
		  public void rasterImg(MouseEvent evt){
			 ImageView iw = (ImageView)evt.getTarget();
			 if(iw.getImage()!=null)
				 verificaCor(iw.getImage(), (int)evt.getX(), (int)evt.getY());
		  }
	 
	 @FXML	
	 public void salvar() {
		 if(img3 != null) {
			 FileChooser fileChooser = new FileChooser();
			 fileChooser.getExtensionFilters().add(
					 new FileChooser.ExtensionFilter("Imagem", "*.png"));
			 File file = fileChooser.showSaveDialog(null);
			 if(file != null){
				 BufferedImage bImg = SwingFXUtils.fromFXImage(img3,null);
				 try {
					 ImageIO.write(bImg, "PNG", file);
				 }catch (IOException e) {
					e.printStackTrace();
				}
			 }
		 }else {
			 mostraMensagem("Não é possível salvar a imagem, não há imagem modificada", AlertType.ERROR);
		 }
	 }
	 
	 @FXML 
	 public void abreHistograma(ActionEvent event) {
		 
		 try {
			 
			 Stage stage = new Stage();
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("Histograma.fxml"));
			 Parent root = loader.load();
			 stage.setScene(new Scene(root));
			 stage.setTitle("Histograma");
			 //stage.initModality(Modality.WINDOW_MODAL);
			 stage.initOwner(((Node)event.getSource()).getScene().getWindow());
			 stage.show();
			 
			 Histograma_Controller controller = (Histograma_Controller)loader.getController();
			 
			 if(img1 != null)
				 PDI.getGrafico(img1, controller.grafico1);
			 if(img2 != null)
			 	PDI.getGrafico(img2, controller.grafico2);
			 if(img3 != null)
			 	PDI.getGrafico(img3, controller.grafico3);
			 
		 }catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
	private void atualizaImagem3() {
		imageView3.setImage(img3);
		imageView3.setFitWidth(img3.getWidth());
		imageView3.setFitHeight(img3.getHeight());
	}
	
	 private Image abreImagem(ImageView imageView, Image image) {
		File f = selecionaImagem();
		if(f!= null) {
			image = new Image(f.toURI().toString());
			imageView.setImage(image);
			imageView.setFitHeight(image.getWidth());
			imageView.setFitHeight(image.getHeight());
			return image;
		}
		
		return null;
	}
	
	private File selecionaImagem() {
		   FileChooser fileChooser = new FileChooser();
		   fileChooser.getExtensionFilters().add(new 
				   FileChooser.ExtensionFilter(
						   "Imagens", "*.jpg", "*.JPG", 
						   "*.png", "*.PNG", "*.gif", "*.GIF", 
						   "*.bmp", "*.BMP")); 	
		   fileChooser.setInitialDirectory(new File(
				   "C:/Users/Klait/Desktop/PDI"));
		   File imgSelec = fileChooser.showOpenDialog(null);
		   try {
			   if (imgSelec != null) {
			    return imgSelec;
			   }
		   } catch (Exception e) {
			e.printStackTrace();
		   }
		   return null;
	}
	
	private void mostraMensagem (String msg, AlertType tipo) { // recebe uma String por paremetro
		
		Alert a = new Alert (AlertType.INFORMATION);
		
		a.setHeaderText(null); // modificar mensagem
		a.setContentText(msg);
		a.show();
	}
	
}
