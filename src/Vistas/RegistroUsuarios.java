package Vistas;

import Dominio.Usuari;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: javierferrer
 * Date: 20/10/12
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
public class RegistroUsuarios
{

	private JTextField passwordTextField;
	private JButton cancelarButton;
	private JButton aceptarButton;
	private JTextField usernameTextField;

	public void setData( Usuari data )
	{
		usernameTextField.setText( data.getUsername() );
		passwordTextField.setText( data.getPassword() );
	}

	public void getData( Usuari data )
	{
		data.setUsername( usernameTextField.getText() );
		data.setPassword( passwordTextField.getText() );
	}

	public boolean isModified( Usuari data )
	{
		if ( usernameTextField.getText() != null ? !usernameTextField.getText().equals( data.getUsername() ) : data.getUsername() != null )
		{
			return true;
		}
		if ( passwordTextField.getText() != null ? !passwordTextField.getText().equals( data.getPassword() ) : data.getPassword() != null )
		{
			return true;
		}
		return false;
	}
}
