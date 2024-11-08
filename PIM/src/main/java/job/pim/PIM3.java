package job.pim4;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.PixelGrabber;

public class PIM4 extends JFrame {
    private JButton openButton;
    private JLabel imageLabel;

    public PIM4() {
        // Configurar la ventana principal
        setTitle("PIM4");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el botón "Abrir"
        openButton = new JButton("Abrir");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Crear un objeto JFileChooser
                JFileChooser fileChooser = new JFileChooser();

                // Agregar un filtro para solo permitir archivos de imagen
                FileFilter imageFilter = new FileNameExtensionFilter("Archivos de imagen", "jpg", "jpeg", "png", "gif");
                fileChooser.setFileFilter(imageFilter);

                // Mostrar el cuadro de diálogo "Abrir archivo"
                int returnValue = fileChooser.showOpenDialog(null);

                // Comprobar si se seleccionó un archivo
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());

                    // Mostrar la imagen en la ventana
                    displayImage(selectedFile);
                }
            }
        });

        // Crear el JLabel para mostrar la imagen
        imageLabel = new JLabel();

        // Agregar el botón y el JLabel a la ventana
        getContentPane().add(openButton, "North");
        getContentPane().add(imageLabel, "Center");

        // Mostrar la ventana
        setVisible(true);
        
        // Crear el botón "PixelGrabber"
JButton pixelGrabberButton = new JButton("PixelGrabber");
pixelGrabberButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // Llamar al método para procesar el PixelGrabber
        processPixelGrabber();
    }
});

// Agregar el botón al panel
getContentPane().add(pixelGrabberButton, "South");

    }

    private void displayImage(File file) {
        try {
            // Leer la imagen desde el archivo seleccionado
            BufferedImage image = ImageIO.read(file);

            // Redimensionar la imagen si es necesario
            ImageIcon icon = new ImageIcon(image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.SCALE_SMOOTH));

            // Mostrar la imagen en el JLabel
            imageLabel.setIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    private void processPixelGrabber() {
    // Obtener la imagen del JLabel
    ImageIcon icon = (ImageIcon) imageLabel.getIcon();
    Image image = icon.getImage();

    // Obtener las dimensiones de la imagen
    int width = image.getWidth(null);
    int height = image.getHeight(null);

    // Crear un arreglo para almacenar los píxeles
    int[] pixels = new int[width * height];

    // Crear el objeto PixelGrabber
    PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);

    try {
        // Realizar la operación de PixelGrabber
        pixelGrabber.grabPixels();

        // Procesar los píxeles obtenidos
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = pixels[i * width + j];
                // Realizar la operación deseada con el píxel
                // por ejemplo, imprimir el valor RGB
                System.out.println("Pixel [" + i + "," + j + "]: " + pixel);
                
                
                int red = (pixel >> 16) & 0xFF;     // Componente rojo
                int green = (pixel >> 8) & 0xFF;    // Componente verde
                int blue = pixel & 0xFF;            // Componente azul

                // Hacer algo con los valores RGB del píxel
                // Por ejemplo, imprimirlos en la consola
                System.out.println("Valores RGB del píxel (" + i + ", " + j + "):");
                System.out.println("Rojo: " + red);
                System.out.println("Verde: " + green);
                System.out.println("Azul: " + blue);
            }
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}


    
    public static void main(String[] args) {
        // Crear y mostrar la ventana principal
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PIM4();
            }
        });
    }
}